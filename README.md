# Base de Datos 2 - Material de estudio

Repositorio de apoyo para la materia Base de Datos 2. No es un proyecto único de software, sino una carpeta de trabajo organizada con teoría, resúmenes, parciales, finales, enunciados y resoluciones de trabajos prácticos, además de varios ejemplos en Java/Spring/Hibernate.

## Contenido general

- `bibliografia/`: bibliografía y material de referencia sobre persistencia, ORMs y NoSQL.
- `resumenes/`: resúmenes propios para repaso y preparación de parciales.
- `resumenes_de_otras_personas/`: resúmenes de terceros que sirven como complemento.
- `practicas_viejas/`: guías de prácticos de años anteriores.
- `teorias_viejas/`: teoría antigua, apuntes históricos y material de cursadas previas.
- `teorias_2026/`: material de teoría de la cursada 2026, en PDF, PPTX y videos.
- `trabajos_practicos/enunciados/`: enunciados de los TP de 2026 y material auxiliar.
- `trabajos_practicos/resoluciones/`: resoluciones de los TP, tanto en PDF como en proyectos Maven.
- `parciales_finales_viejos/`: parciales, finales, repasos, simulacros y notas sobre temas tomados.
- `LICENSE`: licencia GNU GPL v3.

## Material de teoría

La carpeta `teorias_2026/` concentra el material más actual de la materia. Allí hay clases introductorias, contenidos sobre mapping y JPA, Hibernate, Spring Data JPA, MongoDB, NoSQL, escalabilidad y modelos columnares. También aparecen videos de clase y un ejemplo práctico de empleados para acompañar los temas de persistencia.

En `teorias_viejas/` quedan los materiales anteriores: apuntes, resúmenes, clases de 2019 y teoría de años previos. Esta parte sirve para repasar enfoques históricos y comparar cómo fue cambiando el programa.

## Trabajos prácticos

Los enunciados de los TP están en `trabajos_practicos/enunciados/`. Además del material principal de TP1 a TP4, hay un paquete auxiliar para TP3 con un generador en JavaScript y un archivo JSON de datos adicionales.

Las resoluciones están en `trabajos_practicos/resoluciones/`:

- `codigo_tp1/`: resolución técnica basada en Spring Boot, Hibernate y acceso manual a repositorios.
- `codigo_tp2/`: resolución técnica basada en Spring Boot y Spring Data JPA.
- PDFs de resolución de TP1 a TP4 para repasar la parte teórica y conceptual.

### Resumen de las resoluciones en Java

`codigo_tp1/codigo` usa Java 17, Spring Boot 3.2.3, Hibernate 6.4 y MySQL. Incluye configuración de Hibernate, inicialización de base, entidades del dominio, repositorios, servicios y tests con H2.

`codigo_tp2/codigo` también usa Java 17 y Spring Boot 3.2.3, pero migra a Spring Data JPA. Suma repositorios por interfaz, una capa de servicios, entidades del dominio y un DTO para consultas resumidas.

## Ejemplo de empleados

Dentro de `teorias_2026/empleados_ejemplo/empleados` hay otro proyecto Maven con un caso de empleados. Ese ejemplo incluye configuración de Hibernate, inicialización de datos, modelo de empleados con variantes como vendedor, operario y administrativo, y pruebas de repositorio y servicio. Se usa como apoyo para entender herencia, persistencia y configuración de Spring/Hibernate.

## Parciales y finales

`parciales_finales_viejos/` reúne parciales y finales anteriores, un simulacro, imágenes de temas y un comentario con lo que tomó el examen en 2025. Es la carpeta más útil para repasar patrones de evaluación, temas repetidos y el estilo de preguntas que suele aparecer.

## Cómo orientarse en el repo

1. Si estás preparando teoría, empezá por `teorias_2026/` y después compará con `teorias_viejas/`.
2. Si querés practicar, revisá primero `trabajos_practicos/enunciados/` y luego `trabajos_practicos/resoluciones/`.
3. Si estás estudiando para parcial o final, combiná `resumenes/`, `resumenes_de_otras_personas/` y `parciales_finales_viejos/`.
4. Si necesitás entender la implementación, abrí los proyectos Maven de `codigo_tp1/`, `codigo_tp2/` y `empleados/`.

## Notas

- Hay carpetas de build generadas, como `target/` y `bin/`, dentro de algunos proyectos Maven.
- El repositorio mezcla material propio, material de cursada y archivos de apoyo de distintos años.
- La estructura está pensada para estudiar, repasar y resolver prácticas, no para un único flujo de ejecución.
