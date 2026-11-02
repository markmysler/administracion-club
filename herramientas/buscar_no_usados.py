# -*- coding: utf-8 -*-
"""Lista candidatos a código no usado en src/main/java: clases sin referencias, métodos cuyo nombre no se invoca en
ningún lado y atributos privados que se asignan pero nunca se leen. Es un análisis por nombres (aproximado): un método
con el mismo nombre que otro usado no aparece; lo que usa el framework (Spring, Vaadin, Binder) se marca aparte.

Uso: python herramientas/buscar_no_usados.py [--tsv]
"""
import os
import re
import sys

RAIZ = 'src/main/java'
MODS = r'(?:public|protected|private|abstract|final|static|synchronized|default)'
RE_METODO = re.compile(r'^\s*(?:@\w+(?:\([^)]*\))?\s*)*(?:' + MODS + r'\s+)*(?:<[^>]+>\s+)?([\w.<>\[\],? ]+)\s+(\w+)\s*\(')
RE_ATRIB = re.compile(r'^\s*(?:@\w+(?:\([^)]*\))?\s*)*private\s+(?:static\s+)?(?:final\s+)?([\w.<>\[\],? ]+)\s+(\w+)\s*(=|;)')
FRAMEWORK_ANOT = ('@Scheduled', '@EventListener', '@TransactionalEventListener', '@Bean', '@PostMapping', '@GetMapping',
                  '@Override', '@ExceptionHandler', '@PostConstruct')
NO_METODO = {'if', 'for', 'while', 'switch', 'catch', 'return', 'new', 'else', 'throw', 'super', 'this', 'try'}


TOKENS = re.compile(r'"""(?:.|\n)*?"""|"(?:\\.|[^"\\\n])*"|\'(?:\\.|[^\'\\\n])*\'|/\*(?:.|\n)*?\*/|//[^\n]*')


def limpiar(texto):
    """Quita comentarios y literales en una sola pasada (una URL dentro de un texto no es un comentario)."""
    def reemplazo(m):
        t = m.group(0)
        if t.startswith('/'):
            return '\n' * t.count('\n')
        return '""' + '\n' * t.count('\n')
    return TOKENS.sub(reemplazo, texto)


archivos = {}
for raiz, _, nombres in os.walk(RAIZ):
    for n in nombres:
        if n.endswith('.java'):
            p = os.path.join(raiz, n).replace('\\', '/')
            archivos[p] = limpiar(open(p, encoding='utf-8').read())

todo = '\n'.join(archivos.values())
resultados = []

for p, texto in archivos.items():
    clase = os.path.basename(p)[:-5]
    paquete = p[len(RAIZ) + 1:].replace('/', '.').rsplit('.', 2)[0].replace('ar.edu.ub.club.', '')
    otros = '\n'.join(t for q, t in archivos.items() if q != p)
    framework_clase = bool(re.search(r'@(Component|Service|Route|SpringComponent|Configuration|RestController|Controller)\b', texto))
    if not re.search(r'\b' + clase + r'\b', otros):
        resultados.append(('clase', paquete, clase, '', 'framework' if framework_clase else 'sin referencias'))
    lineas = texto.split('\n')
    for i, l in enumerate(lineas):
        m = RE_METODO.match(l)
        if m and m.group(2) not in NO_METODO and m.group(2) != clase and '=' not in l.split('(')[0]:
            nombre = m.group(2)
            previo = '\n'.join(lineas[max(0, i - 3):i + 1])
            usos = len(re.findall(r'(?<![\w])' + nombre + r'\s*\(|::' + nombre + r'\b', todo)) - 1
            if usos <= 0:
                motivo = 'sin llamadas'
                if any(a in previo for a in FRAMEWORK_ANOT):
                    motivo = 'framework/override'
                elif re.match(r'(get|set|is)[A-Z]', nombre):
                    motivo = 'accesor (Binder/reflexión posible)'
                elif nombre in ('main', 'toString', 'equals', 'hashCode', 'compareTo'):
                    motivo = 'framework/override'
                resultados.append(('método', paquete, clase, nombre, motivo))
        a = RE_ATRIB.match(l)
        if a:
            nombre = a.group(2)
            lecturas = 0
            for j, l2 in enumerate(lineas):
                if j == i:
                    continue
                for mo in re.finditer(r'(?<![\w.])(?:this\.)?' + nombre + r'\b', l2):
                    resto = l2[mo.end():].lstrip()
                    if resto.startswith('=') and not resto.startswith('=='):
                        continue
                    lecturas += 1
            if lecturas == 0:
                resultados.append(('atributo', paquete, clase, nombre, 'se asigna pero no se lee' if a.group(3) == ';' or True else ''))

if '--tsv' in sys.argv:
    for r in resultados:
        print('\t'.join(r))
else:
    for tipo in ('clase', 'método', 'atributo'):
        fil = [r for r in resultados if r[0] == tipo]
        print(f'== {tipo}: {len(fil)}')
        for r in sorted(fil, key=lambda r: (r[4], r[1], r[2], r[3])):
            print(f'  [{r[4]}] {r[1]}.{r[2]}{("." + r[3]) if r[3] else ""}')
