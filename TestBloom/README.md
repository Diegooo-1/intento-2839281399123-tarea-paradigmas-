# Sistema de Pruebas - Taxonomía de Bloom

## Descripción
Este programa en Java Swing administra pruebas basadas en la taxonomía de Bloom, permitiendo cargar preguntas desde un archivo CSV, responderlas, ver un resumen de resultados y revisar las respuestas.

## Requisitos
- Java JDK 8 o superior.
- Un archivo CSV con preguntas en el formato especificado.

## Formato del Archivo CSV
El archivo `preguntas.csv` debe tener 6 columnas separadas por comas:
- Pregunta: El texto de la pregunta.
- Tipo: `MULTIPLE_CHOICE` o `TRUE_FALSE`.
- Opciones: Para `MULTIPLE_CHOICE`, opciones separadas por `;`. Para `TRUE_FALSE`, dejar vacío (`,,`).
- Respuesta Correcta: La respuesta correcta.
- Nivel de Bloom: `REMEMBER`, `UNDERSTAND`, `APPLY`, `ANALYZE`, `EVALUATE`, `CREATE`.
- Tiempo: Tiempo estimado en segundos.

Ejemplo: