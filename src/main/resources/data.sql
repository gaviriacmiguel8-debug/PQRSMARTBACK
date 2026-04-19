-- Insertar datos en la tabla Tipos_Identificacion
INSERT INTO Tipos_Identificacion (Nombre_Tipo_Identificacion) VALUES ('Cédula de ciudadanía');
INSERT INTO Tipos_Identificacion (Nombre_Tipo_Identificacion) VALUES ('Cédula de extranjería');
INSERT INTO Tipos_Identificacion (Nombre_Tipo_Identificacion) VALUES ('Numero Único de Identificación Personal (NUIP)');
INSERT INTO Tipos_Identificacion (Nombre_Tipo_Identificacion) VALUES ('Pasaporte');
INSERT INTO Tipos_Identificacion (Nombre_Tipo_Identificacion) VALUES ('Permiso Especial');
INSERT INTO Tipos_Identificacion (Nombre_Tipo_Identificacion) VALUES ('Permiso por Protección Personal (PPT)');
INSERT INTO Tipos_Identificacion (Nombre_Tipo_Identificacion) VALUES ('Numero de Identificación Tributaria (NIT)');

-- Insertar datos en la tabla Tipos_Persona
INSERT INTO Tipos_Persona (Nombre_Tipo_Persona) VALUES ('Persona Natural');
INSERT INTO Tipos_Persona (Nombre_Tipo_Persona) VALUES ('Persona Juridica');

-- Estados de solicitud
INSERT INTO Estados (Descripcion) VALUES ('ACTIVADO');
INSERT INTO Estados (Descripcion) VALUES ('DESACTIVADO');

-- Insertar datos en la tabla Tipos_Solicitud
INSERT INTO tipos_solicitud (nombre_tipo_solicitud) values ('Peticion');
INSERT INTO tipos_solicitud (nombre_tipo_solicitud) values ('Queja');
INSERT INTO tipos_solicitud (nombre_tipo_solicitud) values ('Reclamo');
INSERT INTO tipos_solicitud (nombre_tipo_solicitud) values ('Sugerencia');

-- Insertar datos en la tabla estados_solicitud
INSERT INTO estados_solicitud(nombre_estado_solicitud) values ('Pendiente');
INSERT INTO estados_solicitud(nombre_estado_solicitud) values ('Finalizado');
INSERT INTO estados_solicitud(nombre_estado_solicitud) values ('Rechazado');
INSERT INTO estados_solicitud(nombre_estado_solicitud) values ('Cancelado');

-- Insertar Datos en la tabla Dependencias
INSERT INTO dependencias (nombre_dependencia, estado) values ('Secretaria de Salud', 1);
INSERT INTO dependencias (nombre_dependencia, estado) values ('Secretaria de Educación', 1);
INSERT INTO dependencias (nombre_dependencia, estado) values ('Secretaria de Gobierno', 1);
INSERT INTO dependencias (nombre_dependencia, estado) values ('Dirección Financiera', 1);
INSERT INTO dependencias (nombre_dependencia, estado) values ('Secretaria de Planeación' , 1);
INSERT INTO dependencias (nombre_dependencia, estado) values ('UMATA', 1);
INSERT INTO dependencias (nombre_dependencia, estado) values ('N\A', 1);
-- Insertar Datos en la tabla Categorias
INSERT INTO categorias (id_dependencia, nombre_categoria, estado) values (1,'Salud publica', 1);
INSERT INTO categorias (id_dependencia, nombre_categoria, estado) values (1,'Afiliación', 1);
INSERT INTO categorias (id_dependencia, nombre_categoria, estado) values (1,'Infraestructura hospitalaria', 1);

INSERT INTO categorias (id_dependencia, nombre_categoria, estado) values (2,'Programa de alimentación escolar (PAE)', 1);
INSERT INTO categorias (id_dependencia, nombre_categoria, estado) values (2,'Infraestructura educativa', 1);
INSERT INTO categorias (id_dependencia, nombre_categoria, estado) values (2,'Cobertura educativa', 1);
INSERT INTO categorias (id_dependencia, nombre_categoria, estado) values (2,'Educación continua', 1);
INSERT INTO categorias (id_dependencia, nombre_categoria, estado) values (2,'Educación Superior', 1);

INSERT INTO categorias (id_dependencia, nombre_categoria, estado) values (3,'Seguridad', 1);
INSERT INTO categorias (id_dependencia, nombre_categoria, estado) values (3,'Victimas', 1);
INSERT INTO categorias (id_dependencia, nombre_categoria, estado) values (3,'Contratación', 1);

INSERT INTO categorias (id_dependencia, nombre_categoria, estado) values (4,'Impuestos', 1);
INSERT INTO categorias (id_dependencia, nombre_categoria, estado) values (4,'Pagos proveedores', 1);
INSERT INTO categorias (id_dependencia, nombre_categoria, estado) values (4,'Presupuesto', 1);
INSERT INTO categorias (id_dependencia, nombre_categoria, estado) values (4,'Contabilidad', 1);

INSERT INTO categorias (id_dependencia, nombre_categoria, estado) values (5,'Infraestructura', 1);
INSERT INTO categorias (id_dependencia, nombre_categoria, estado) values (5,'Certificados', 1);
INSERT INTO categorias (id_dependencia, nombre_categoria, estado) values (5,'Gestión del riesgo', 1);

INSERT INTO categorias (id_dependencia, nombre_categoria, estado) values (6,'Asistencia técnica', 1);
INSERT INTO categorias (id_dependencia, nombre_categoria, estado) values (6,'Medio ambiente', 1);


INSERT INTO Estados_Usuario ( Estados) VALUES ( 'INACTIVO');
INSERT INTO Estados_Usuario ( Estados) VALUES ( 'ACTIVO');
INSERT INTO Estados_Usuario ( Estados) VALUES ( 'DESACTIVO');

