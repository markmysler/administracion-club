# -*- coding: utf-8 -*-
"""Verifica que TODA clase, interfaz, enumeración, record, método y constructor (incluidos los privados) tenga
Javadoc con la línea «Casos de uso:» y sin marcas «PENDIENTE».

Uso:
    python herramientas/verificar_javadoc.py [carpeta ...]        (por omisión src/main/java)
    python herramientas/verificar_javadoc.py --stubs                (además lista los métodos sin implementar)

Sale con código 1 si encuentra algún problema.
"""
import os
import re
import sys

MODIFICADORES = r'(?:public|protected|private|abstract|final|static|sealed|non-sealed|default|synchronized|native|strictfp|transient)'
RE_TIPO = re.compile(r'^\s*(?:@\w+(?:\([^)]*\))?\s+)*(?:' + MODIFICADORES + r'\s+)*(class|interface|enum|record|@interface)\s+(\w+)')
RE_METODO = re.compile(r'^\s*(?:' + MODIFICADORES + r'\s+)*(?:<[^>]+>\s+)?(?:[\w.$<>\[\]?, ]+\s+)?(\w+)\s*\(')
NO_METODO = {'return', 'new', 'if', 'for', 'while', 'switch', 'catch', 'else', 'throw', 'super', 'this', 'try',
             'do', 'case', 'assert', 'synchronized', 'yield'}


def limpiar(linea, en_bloque):
    """Quita comentarios y literales para contar llaves. Devuelve (texto, en_bloque)."""
    salida, i = '', 0
    while i < len(linea):
        if en_bloque:
            fin = linea.find('*/', i)
            if fin < 0:
                return salida, True
            i, en_bloque = fin + 2, False
            continue
        c = linea[i]
        if linea.startswith('/*', i):
            en_bloque = True
            i += 2
            continue
        if linea.startswith('//', i):
            break
        if c in '"\'':
            if linea.startswith('"""', i):
                fin = linea.find('"""', i + 3)
                i = len(linea) if fin < 0 else fin + 3
                salida += '""'
                continue
            j = i + 1
            while j < len(linea) and linea[j] != c:
                j += 2 if linea[j] == '\\' else 1
            salida += c + c
            i = j + 1
            continue
        salida += c
        i += 1
    return salida, en_bloque


def javadoc_previo(lineas, idx):
    """Devuelve el texto del Javadoc inmediatamente anterior a la línea idx (saltando anotaciones) o None."""
    j = idx - 1
    while j >= 0:
        s = lineas[j].strip()
        if s == '' or (s.startswith('@') and not s.startswith('@param')):
            j -= 1
            continue
        if s.endswith('*/'):
            fin = j
            while j >= 0 and '/**' not in lineas[j]:
                if '/*' in lineas[j] and '/**' not in lineas[j]:
                    return None
                j -= 1
            if j < 0:
                return None
            return '\n'.join(lineas[j:fin + 1])
        # anotación de varias líneas: )
        if s.endswith(')') or s.endswith(','):
            j -= 1
            continue
        return None
    return None


def revisar(ruta):
    problemas, stubs = [], []
    lineas = open(ruta, encoding='utf-8').read().split('\n')
    profundidad, en_bloque = 0, False
    cuerpos_tipo = []          # profundidades a las que están los miembros de cada tipo abierto
    for idx, linea in enumerate(lineas):
        texto, en_bloque_nuevo = limpiar(linea, en_bloque)
        estaba_en_bloque = en_bloque
        en_bloque = en_bloque_nuevo
        if not estaba_en_bloque and texto.strip():
            nivel_miembro = cuerpos_tipo and profundidad == cuerpos_tipo[-1]
            m_tipo = RE_TIPO.match(texto)
            es_decl = False
            if m_tipo and (profundidad == 0 or nivel_miembro):
                es_decl = True
                nombre = m_tipo.group(2)
                doc = javadoc_previo(lineas, idx)
                verificar(doc, f'{m_tipo.group(1)} {nombre}', ruta, idx, problemas)
                cuerpos_tipo.append(profundidad + 1)
            elif nivel_miembro and not texto.strip().startswith('@'):
                m = RE_METODO.match(texto)
                if m and m.group(1) not in NO_METODO:
                    previo = texto[:m.end(1)]
                    if '=' not in previo and '.' not in previo.split('(')[0].split()[-1] and '->' not in previo:
                        doc = javadoc_previo(lineas, idx)
                        verificar(doc, f'método {m.group(1)}', ruta, idx, problemas)
                        if '"PENDIENTE"' in ''.join(lineas[idx:idx + 4]) or '// PENDIENTE' in ''.join(lineas[idx:idx + 4]):
                            stubs.append(f'{ruta}:{idx + 1}: {m.group(1)}')
        for c in texto:
            if c == '{':
                profundidad += 1
            elif c == '}':
                profundidad -= 1
                while cuerpos_tipo and profundidad < cuerpos_tipo[-1]:
                    cuerpos_tipo.pop()
    return problemas, stubs


def verificar(doc, que, ruta, idx, problemas):
    lugar = f'{ruta}:{idx + 1}: {que}'
    if doc is None:
        problemas.append(lugar + ' sin Javadoc')
    elif 'Casos de uso:' not in doc:
        problemas.append(lugar + ' sin la línea «Casos de uso:»')
    elif 'PENDIENTE' in doc:
        problemas.append(lugar + ' con Javadoc PENDIENTE')


def main():
    args = [a for a in sys.argv[1:] if not a.startswith('--')]
    carpetas = args or ['src/main/java']
    todos, todos_stubs, archivos = [], [], 0
    for carpeta in carpetas:
        for raiz, _, nombres in os.walk(carpeta):
            for n in sorted(nombres):
                if n.endswith('.java'):
                    archivos += 1
                    p, s = revisar(os.path.join(raiz, n))
                    todos += p
                    todos_stubs += s
    for p in todos:
        print(p)
    if '--stubs' in sys.argv:
        for s in todos_stubs:
            print('SIN IMPLEMENTAR', s)
        print(f'{len(todos_stubs)} métodos sin implementar')
    print(f'{archivos} archivos revisados, {len(todos)} problemas de documentación')
    sys.exit(1 if todos else 0)


if __name__ == '__main__':
    main()
