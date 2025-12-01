

# AddQR - Gestión de Inventario y Activos 

Este proyecto es una aplicación móvil nativa para Android diseñada para la gestión de inventario y el seguimiento de activos utilizando códigos QR. Cumple con los requisitos EV3 (Entorno de Verificación 3) para una aplicación completa basada en Java y SQLite.

## 👥 Repositorio y Autores

| Elemento | Detalle |
| :--- | :--- |
| **Rama Principal** | `main` |
| **Rama de Desarrollo** | `development` |
| **Usuario/Equipo** | *BenjaminAPP* |
| **Autor Líder / Creador** | **Benjamin** - Desarrollo completo, arquitectura, lógica y testing. |

## 🛠️ Requisitos Técnicos 

La aplicación está configurada con las siguientes especificaciones:

  * **Lenguaje:** Java
  * **Base de Datos:** SQLite nativa de Android.
  * **Actividades:** 10 Activities distintas para cubrir el flujo completo.
  * **Funcionalidad Hardware (2):**
      * *Cámara:* Escaneo de códigos QR para identificación y validación de activos.
      * *GPS/Ubicación:* Registro de la ubicación geográfica al mover o actualizar un activo.
      * *(Nota: El micrófono y el calendario no se usan en este alcance, se prioriza QR/GPS por la temática de inventario).*
  * **API Externa (1):** Integración con una API de Mapas Estáticos (ej. Google Maps Static API) o Geocoding para visualizar la última ubicación registrada de un activo.
  * **SDK Mínimo:** API 24 (Android 7.0 Nougat) o superior.

## Flujo de la Aplicación y 10 Activities

El flujo se centra en el ciclo de vida de un activo, desde su registro hasta su seguimiento de ubicación.

| \# | Activity | Descripción y Flujo | Hardware/API |
| :--- | :--- | :--- | :--- |
| **1** | `MainActivity` | Menú principal y acceso rápido a funciones clave. | N/A |
| **2** | `ScanQRActivity` | Captura de Imagen/Cámara. Inicia el escáner QR para identificar un activo y redirige a *AssetDetailActivity*. | **CÁMARA** |
| **3** | `NewAssetActivity` | Formulario para registrar un activo nuevo en la BD. Genera un QR único. | N/A |
| **4** | `AssetListActivity` | Lista completa de todos los activos en el inventario. | N/A |
| **5** | `AssetDetailActivity` | Muestra la información completa de un activo, su estado y su última ubicación. Contiene el botón para "Mover Activo". | GPS (disparado) |
| **6** | `UpdateLocationActivity` | Funcionalidad de Hardware (GPS). Actividad dedicada a obtener la ubicación actual (Lat/Lon) y guardarla como un nuevo registro en la BD. | **GPS** |
| **7** | `LocationHistoryActivity` | Muestra la lista cronológica de todos los movimientos de un activo específico. | N/A |
| **8** | `EditAssetActivity` | Formulario para modificar los datos de un activo existente. | N/A |
| **9** | `MapDisplayActivity` | Integración API. Muestra la última coordenada registrada del activo utilizando un servicio de mapas externo. | **API Externa** |
| **10** | `SettingsActivity` | Permite configurar parámetros de la aplicación, como la clave de la API de Mapas. | N/A |

## 🏗️ Ingeniería y Arquitectura

La arquitectura sigue el patrón **Modelo-Vista-Controlador (MVC)**, separando claramente las responsabilidades.

### 1\. Directorio `ui` (Views/Activities)

Contiene las 10 Activities definidas arriba y los adaptadores necesarios para mostrar listas (ej. `AssetAdapter`).

### 2\. Directorio `data` (Persistencia SQLite)

Centraliza la gestión de datos.

  * **Modelos (`Asset`, `LocationRecord`):** Objetos de datos.
  * **`DbHelper`:** Hereda de `SQLiteOpenHelper`, responsable de crear (`onCreate`) y actualizar (`onUpgrade`) la estructura de las tablas (`Assets` y `LocationRecords`).
  * **`AssetDAO` (Data Access Object):** Contiene los métodos CRUD (`insertAsset`, `getAssetById`, `addLocationRecord`, etc.) para interactuar con la BD de forma segura.


    
### Diagrama de Clases (UML) 
<img width="2262" height="983" alt="image" src="https://github.com/user-attachments/assets/4cfd5241-4351-45a3-8cde-25783005dbed" />



### Diagrama de Flujo (Actividades)
<img width="755" height="1218" alt="image" src="https://github.com/user-attachments/assets/34acf04f-75f9-4869-bb95-087924219f45" />

AddQR - Gestión de Inventario y Activos

Este proyecto es una aplicación móvil nativa para Android diseñada para la gestión de inventario y el seguimiento de activos utilizando códigos QR. Cumple con los requisitos EV3 (Entorno de Verificación 3) para una aplicación completa basada en Java y SQLite.

👥 Repositorio y Autores

Elemento

Detalle

Rama Principal

main

Rama de Desarrollo

development

Usuario/Equipo

BenjaminAPP

Autor Líder / Creador

Benjamin - Desarrollo completo, arquitectura, lógica y testing.

🛠️ Requisitos Técnicos

La aplicación está configurada con las siguientes especificaciones:

Lenguaje: Java

Base de Datos: SQLite nativa de Android.

Actividades: 10 Activities distintas para cubrir el flujo completo.

Funcionalidad Hardware (2):

Cámara: Escaneo de códigos QR para identificación y validación de activos.

GPS/Ubicación: Registro de la ubicación geográfica al mover o actualizar un activo.

(Nota: El micrófono y el calendario no se usan en este alcance, se prioriza QR/GPS por la temática de inventario).

API Externa (1): Integración con una API de Mapas Estáticos (ej. Google Maps Static API) o Geocoding para visualizar la última ubicación registrada de un activo.

SDK Mínimo: API 24 (Android 7.0 Nougat) o superior.

Flujo de la Aplicación y 10 Activities

El flujo se centra en el ciclo de vida de un activo, desde su registro hasta su seguimiento de ubicación.

#

Activity

Descripción y Flujo

Hardware/API

1

MainActivity

Menú principal y acceso rápido a funciones clave.

N/A

2

ScanQRActivity

Captura de Imagen/Cámara. Inicia el escáner QR para identificar un activo y redirige a AssetDetailActivity.

CÁMARA

3

NewAssetActivity

Formulario para registrar un activo nuevo en la BD. Genera un QR único.

N/A

4

AssetListActivity

Lista completa de todos los activos en el inventario.

N/A

5

AssetDetailActivity

Muestra la información completa de un activo, su estado y su última ubicación. Contiene el botón para "Mover Activo".

GPS (disparado)

6

UpdateLocationActivity

Funcionalidad de Hardware (GPS). Actividad dedicada a obtener la ubicación actual (Lat/Lon) y guardarla como un nuevo registro en la BD.

GPS

7

LocationHistoryActivity

Muestra la lista cronológica de todos los movimientos de un activo específico.

N/A

8

EditAssetActivity

Formulario para modificar los datos de un activo existente.

N/A

9

MapDisplayActivity

Integración API. Muestra la última coordenada registrada del activo utilizando un servicio de mapas externo.

API Externa

10

SettingsActivity

Permite configurar parámetros de la aplicación, como la clave de la API de Mapas.

N/A

🏗️ Ingeniería y Arquitectura

La arquitectura sigue el patrón Modelo-Vista-Controlador (MVC), separando claramente las responsabilidades.

Directorio ui (Views/Activities)
Contiene las 10 Activities definidas arriba y los adaptadores necesarios para mostrar listas (ej. AssetAdapter).

Directorio data (Persistencia SQLite)
Centraliza la gestión de datos.

Modelos (Asset, LocationRecord): Objetos de datos.

DbHelper: Hereda de SQLiteOpenHelper, responsable de crear (onCreate) y actualizar (onUpgrade) la estructura de las tablas (Assets y LocationRecords).

AssetDAO (Data Access Object): Contiene los métodos CRUD (insertAsset, getAssetById, addLocationRecord, etc.) para interactuar con la BD de forma segura.

🎨 Diseño de la Aplicación (Visual y UX)

Tras una mejora visual completa, la aplicación adopta el tema Asset Hunter PRO, diseñado para ser moderno, profesional y de alto contraste, ideal para su uso en entornos de trabajo.

Paleta de Colores

Elemento

Color

Código Hex

Propósito

Fondo Principal

Azul Marino Oscuro

#15202B

Base del Modo Oscuro. Proporciona un entorno visual limpio y sin distracciones.

Color de Acción (Primario)

Naranja Neón

#FF8C00

Utilizado en los botones más críticos (Ej: "Escanear Activo", "Registrar GPS"). Genera el mayor contraste.

Color Informativo

Azul Brillante

#00A3FF

Usado para títulos, iconos de navegación y resaltar información clave no crítica (Ej: estado "Disponible").

Fondo de Tarjetas / Campos

Gris Oscuro Azulado

#1A2C39

Da profundidad a los elementos interactivos y a las tarjetas de detalle, separándolos del fondo.

Color de Texto Base

Blanco

#FFFFFF

Garantiza la máxima legibilidad sobre los fondos oscuros.

Componentes de Interfaz

Tipografía: Clara y simple (se recomienda usar la fuente por defecto de Android o una Sans-Serif moderna) para una lectura rápida.

Diseño: Se utiliza el Diseño Basado en Tarjetas (CardView) para agrupar la información compleja (como en AssetDetailActivity), haciendo que los bloques de información (descripción, coordenadas) sean visualmente distintos.

Campos de Formulario: Los EditText y TextInputLayout usan el fondo Gris Oscuro (#1A2C39) con bordes en un tono más claro, lo que proporciona un look and feel sofisticado y funcional en el modo oscuro.

Botones de Navegación: En MainActivity, los botones de función principal (Escanear, Añadir, Inventario) están agrupados y los botones de utilidad (Configuración, Acerca De, Ver en Mapa) se encuentran en un footer con iconos para un acceso rápido y discreto.

Consistencia Visual

Todos los layouts (desde MainActivity hasta EditAssetActivity y MapDisplayActivity) aplican la paleta de Azul Marino, logrando una experiencia de usuario coherente en toda la aplicación, tal como se refleja en las últimas capturas de pantalla.

Diagrama de Clases (UML)

Diagrama de Flujo (Actividades)

Diagrama de Secuencia: Registro de Ubicación (Hardware GPS)
    


### Diagrama de Secuencia: Registro de Ubicación (Hardware GPS)
<img width="1155" height="889" alt="image" src="https://github.com/user-attachments/assets/5ae6001f-55fd-4471-847f-269ebc59f1b9" />


