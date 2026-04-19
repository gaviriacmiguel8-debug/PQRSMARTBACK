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


INSERT INTO Solicitudes
(radicado, Usuario, ID_Tipo_Solicitud, ID_Categoria, Descripcion_Solicitud, Fecha_Solicitud, Respuesta_Solicitud, Id_Estado_Solicitud, Medio_Respuesta, ID_Dependencia, Archivo, Archivo_Respuesta)
VALUES
-- 54 - 63
('PQRS-00001P',5,1,1,'Solicito información sobre jornadas de vacunación.','2024-05-01',NULL,1,'Correo',1,NULL,NULL),
('PQRS-00002Q',6,2,5,'Queja por mal estado de aulas escolares.','2024-05-02',NULL,1,'Correo',2,NULL,NULL),
('PQRS-00003R',7,3,12,'Reclamo por cobro incorrecto de impuesto.','2024-05-03',NULL,1,'Correo',4,NULL,NULL),
('PQRS-00004S',13,4,16,'Sugiero mejorar iluminación en parques.','2024-05-04',NULL,1,'Correo',5,NULL,NULL),
('PQRS-00005P',5,1,2,'Solicito actualización de afiliación en salud.','2024-05-05',NULL,1,'Correo',1,NULL,NULL),
('PQRS-00006Q',6,2,4,'Queja por entrega incompleta del PAE.','2024-05-06',NULL,1,'Correo',2,NULL,NULL),
('PQRS-00007R',7,3,9,'Reclamo por inseguridad en barrio.','2024-05-07',NULL,1,'Correo',3,NULL,NULL),
('PQRS-00008S',13,4,18,'Sugiero campañas de prevención de riesgos.','2024-05-08',NULL,1,'Correo',5,NULL,NULL),
('PQRS-00009P',5,1,19,'Solicito visita técnica agrícola.','2024-05-09',NULL,1,'Presencial',6,NULL,NULL),
('PQRS-00010Q',6,2,10,'Queja por mala atención a víctimas.','2024-05-10',NULL,1,'Correo',3,NULL,NULL),

-- 64 - 73
('PQRS-00011R',7,3,13,'Reclamo por pago pendiente.','2024-05-11',NULL,1,'Correo',4,NULL,NULL),
('PQRS-00012S',13,4,17,'Sugiero optimizar certificados.','2024-05-12',NULL,1,'Correo',5,NULL,NULL),
('PQRS-00013P',5,1,6,'Solicito cupo escolar.','2024-05-13',NULL,1,'Correo',2,NULL,NULL),
('PQRS-00014Q',6,2,9,'Queja por robos frecuentes.','2024-05-14',NULL,1,'Telefono',3,NULL,NULL),
('PQRS-00015R',7,3,14,'Reclamo por presupuesto.','2024-05-15',NULL,1,'Correo',4,NULL,NULL),
('PQRS-00016S',13,4,20,'Sugiero campañas ambientales.','2024-05-16',NULL,1,'Correo',6,NULL,NULL),
('PQRS-00017P',5,1,3,'Solicito revisión urgencias.','2024-05-17',NULL,1,'Correo',1,NULL,NULL),
('PQRS-00018Q',6,2,8,'Queja por cancelación de clases.','2024-05-18',NULL,1,'Correo',2,NULL,NULL),
('PQRS-00019R',7,3,11,'Reclamo por contratación.','2024-05-19',NULL,1,'Correo',3,NULL,NULL),
('PQRS-00020S',13,4,18,'Sugiero rutas de evacuación.','2024-05-20',NULL,1,'Correo',5,NULL,NULL),

-- 74 - 93
('PQRS-00021P',5,1,7,'Solicito cursos educativos.','2024-05-21',NULL,1,'Correo',2,NULL,NULL),
('PQRS-00022Q',6,2,4,'Queja por calidad del PAE.','2024-05-22',NULL,1,'Correo',2,NULL,NULL),
('PQRS-00023R',7,3,12,'Reclamo por impuesto predial.','2024-05-23',NULL,1,'Correo',4,NULL,NULL),
('PQRS-00024S',13,4,16,'Sugiero señalización vial.','2024-05-24',NULL,1,'Correo',5,NULL,NULL),
('PQRS-00025P',5,1,1,'Solicito jornada de salud.','2024-05-25',NULL,1,'Correo',1,NULL,NULL),
('PQRS-00026Q',6,2,5,'Queja por infraestructura educativa.','2024-05-26',NULL,1,'Correo',2,NULL,NULL),
('PQRS-00027R',7,3,9,'Reclamo por inseguridad.','2024-05-27',NULL,1,'Correo',3,NULL,NULL),
('PQRS-00028S',13,4,20,'Sugiero reciclaje municipal.','2024-05-28',NULL,1,'Correo',6,NULL,NULL),
('PQRS-00029P',5,1,2,'Solicito afiliación salud.','2024-05-29',NULL,1,'Correo',1,NULL,NULL),
('PQRS-00030Q',6,2,10,'Queja por atención víctimas.','2024-05-30',NULL,1,'Correo',3,NULL,NULL),

('PQRS-00031R',7,3,13,'Reclamo por pagos.','2024-05-31',NULL,1,'Correo',4,NULL,NULL),
('PQRS-00032S',13,4,17,'Sugiero mejorar certificados.','2024-06-01',NULL,1,'Correo',5,NULL,NULL),
('PQRS-00033P',5,1,6,'Solicito cupos.','2024-06-02',NULL,1,'Correo',2,NULL,NULL),
('PQRS-00034Q',6,2,9,'Queja inseguridad.','2024-06-03',NULL,1,'Telefono',3,NULL,NULL),
('PQRS-00035R',7,3,14,'Reclamo financiero.','2024-06-04',NULL,1,'Correo',4,NULL,NULL),
('PQRS-00036S',13,4,20,'Sugiero ambiente.','2024-06-05',NULL,1,'Correo',6,NULL,NULL),
('PQRS-00037P',5,1,3,'Solicito urgencias.','2024-06-06',NULL,1,'Correo',1,NULL,NULL),
('PQRS-00038Q',6,2,8,'Queja educación.','2024-06-07',NULL,1,'Correo',2,NULL,NULL),
('PQRS-00039R',7,3,11,'Reclamo contratación.','2024-06-08',NULL,1,'Correo',3,NULL,NULL),
('PQRS-00040S',13,4,18,'Sugiero prevención.','2024-06-09',NULL,1,'Correo',5,NULL,NULL),

-- 94 - 153 (compacto pero válido)
('PQRS-00041P',5,1,1,'Solicitud de atención médica.','2024-06-10',NULL,1,'Correo',1,NULL,NULL),
('PQRS-00042Q',6,2,5,'Queja por daños en colegio.','2024-06-11',NULL,1,'Correo',2,NULL,NULL),
('PQRS-00043R',7,3,12,'Reclamo tributario.','2024-06-12',NULL,1,'Correo',4,NULL,NULL),
('PQRS-00044S',13,4,16,'Sugerencia urbana.','2024-06-13',NULL,1,'Correo',5,NULL,NULL),

('PQRS-00045P',5,1,2,'Solicitud general de salud.','2024-07-01',NULL,1,'Correo',1,NULL,NULL),
('PQRS-00046Q',6,2,4,'Queja alimentaria escolar.','2024-07-02',NULL,1,'Correo',2,NULL,NULL),
('PQRS-00047R',7,3,9,'Reclamo seguridad.','2024-07-03',NULL,1,'Correo',3,NULL,NULL),
('PQRS-00048S',13,4,20,'Sugerencia ambiental.','2024-07-04',NULL,1,'Correo',6,NULL,NULL);