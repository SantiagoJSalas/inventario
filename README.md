# Inventario — Demostración de gestión de pruebas con GitHub

![Pruebas unitarias](https://github.com/SantiagoJSalas/inventario/actions/workflows/tests.yml/badge.svg)

Proyecto académico para el curso **Gestión en Pruebas de Software**.

Implementa un sistema de inventario sencillo en Java para demostrar cómo GitHub
apoya la gestión de pruebas mediante control de versiones, ramas, pruebas
unitarias automatizadas con JUnit 5, integración continua con GitHub Actions
y Pull Requests.

## Funcionalidades

- Registrar productos.
- Consultar productos.
- Calcular el valor total de un producto (precio × cantidad).
- Validar que el precio y la cantidad no sean negativos.
- Calcular el valor total del inventario.

## Tecnologías

- Java 21
- Maven
- JUnit 5
- GitHub Actions

## Cómo ejecutar las pruebas localmente

```bash
mvn -B test
```

El resultado esperado es `BUILD SUCCESS`.

## Integración continua

El workflow `.github/workflows/tests.yml` ejecuta automáticamente las pruebas
en cada `push` y en cada Pull Request dirigido a `main`. Los resultados se
pueden consultar en la pestaña **Actions** del repositorio.
