"""
Genera un resumen visual de los resultados de las pruebas unitarias.

Lee los reportes XML que produce Maven Surefire (target/surefire-reports)
y escribe una tabla en formato Markdown. En GitHub Actions, la tabla se
publica en la página de la ejecución mediante la variable de entorno
GITHUB_STEP_SUMMARY. Si se ejecuta localmente, la imprime en la terminal.
"""

import glob
import os
import xml.etree.ElementTree as ET


def limpiar(texto):
    """Deja el texto en una sola línea y escapa caracteres que rompen la tabla."""
    texto = " ".join((texto or "").split())
    return texto.replace("|", "\\|")


def main():
    reportes = sorted(glob.glob("target/surefire-reports/TEST-*.xml"))
    lineas = ["## 🧪 Resultados de las pruebas unitarias", ""]

    if not reportes:
        lineas.append("⚠️ No se encontraron reportes de pruebas. "
                      "Probablemente el código no compiló: revisa el paso **Ejecutar pruebas unitarias**.")
    else:
        filas = []
        aprobadas = fallidas = errores = omitidas = 0

        for reporte in reportes:
            for caso in ET.parse(reporte).getroot().iter("testcase"):
                nombre = limpiar(caso.get("name"))
                fallo = caso.find("failure")
                error = caso.find("error")

                if fallo is not None:
                    fallidas += 1
                    estado, detalle = "❌ Falló", limpiar(fallo.get("message"))
                elif error is not None:
                    errores += 1
                    estado, detalle = "💥 Error", limpiar(error.get("message") or error.get("type"))
                elif caso.find("skipped") is not None:
                    omitidas += 1
                    estado, detalle = "⏭️ Omitida", ""
                else:
                    aprobadas += 1
                    estado, detalle = "✅ Aprobada", ""

                detalle = f"`{detalle}`" if detalle else ""
                filas.append(f"| {estado} | {nombre} | {detalle} |")

        total = aprobadas + fallidas + errores + omitidas
        veredicto = "✅ **Todas las pruebas pasaron**" if fallidas + errores == 0 \
            else "❌ **Hay pruebas que fallaron**"

        lineas += [
            veredicto,
            "",
            "| Total | ✅ Aprobadas | ❌ Fallidas | 💥 Errores | ⏭️ Omitidas |",
            "|:---:|:---:|:---:|:---:|:---:|",
            f"| {total} | {aprobadas} | {fallidas} | {errores} | {omitidas} |",
            "",
            "| Estado | Prueba | Detalle del fallo |",
            "|---|---|---|",
            *filas,
        ]

    resumen = "\n".join(lineas) + "\n"
    destino = os.environ.get("GITHUB_STEP_SUMMARY")
    if destino:
        with open(destino, "a", encoding="utf-8") as archivo:
            archivo.write(resumen)
    else:
        print(resumen)


if __name__ == "__main__":
    main()
