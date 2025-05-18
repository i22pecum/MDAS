# MDAS

En esta practica se han implementado el Gestor de Entradas y el Gestor de Eventos definidos en la practica 1, además se ha implementado por completo TransaccionMgr sin embargo como este componente contenia demasiadas funciones hemos decidido separarlo en dos componentes, EventoMgr que se encarga de todas las operacios CRUD de los eventos y TransaccionMgr que se encarga de todas las operaciones sobre las entradas y las ventas de las mismas. Ademas se ha implementado parte de UsuarioMgr para hacer el registro y el inicio de sesión tanto de usuarios como de organizadores.

Por último se ha realizado el patron de diseño **Factory** para la creación de los diferentes tipos de entradas.

# Estructura del proyecto

El proyecto esta estructurado de la siguiente forma:
```text
mdas/
├── target/
└── src/
    └── main/
        ├── java/
        │   ├── aux/
        │   ├── dto/
        │   ├── dao/
        │   ├── mgr/
        │   └── main/
        └── resources/
```

Y dentro de cada carpeta encontramos los siguientes archivos:

- **resources** --> Los ficheros *.properties* donde se definen las consultas SQL y la informació para acceder a la base de datos
- **java** --> Los archivos *.java* de la aplicación
- **aux** --> Las clases con funcionalidades auxiliares al programa como son una clase para ayudar a solicitar parametros por la terminal, una clase para ayudar a hacer la conexion a la base de datos y una clase para ayudar a leer las consultas SQL del fichero *.properties*
- **dto**(Data Transfer Objects) --> Las clases donde se almacena la informacion traida de la base de datos
- **dao**(Data Access Objects) --> Las clases que se encargan de acceder a la base de datos
- **mgr** --> Las clases que se encargan de comunicar el main con los daos y ademas de hacer comprobaciones adicionales
- **main** --> La clase donde se define el main de la aplicacion

# Base de datos
Para utilizar una base de datos de MariaDB comun entre todos los miembros del grupo, se han creado varios scripts que se emplean de la siguiente forma:

Para instalar la base de datos y todos tener un mismo usuario dentro de esta para que funcionen los script de importar y exportar hacer:
```bash
sudo ./instalar_bbdd.sh
```

Para cargar los datos dentro de la base de datos habra que tener en el directorio actual el fichero backup.sql y hacer:
```bash
sudo ./importar_bbdd.sh
```

Para exportar los datos de la base de datos habra que hacer:
```bash
sudo ./exportar_bbdd.sh
```
Este comando generara un archivo backup.sql el cual habra que subir a GitHub posteriormente
