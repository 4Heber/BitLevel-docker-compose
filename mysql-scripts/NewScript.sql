-- CREAMOS LA BASE DE DATOS
DROP DATABASE IF EXISTS Bitlevel;
CREATE DATABASE BitLevel;

-- SELECCIONAMOS LA BASE DE DATOS
use BitLevel;

-- 1 TABLA PARA LOS USUARIOS REGISTRADOS
Create table if not exists  UserRegistered (

idVault int, # ID DE LA BILLETERA
tempToken varchar(20), # TOKEN PARA LOGIN CON COOKIES
tokenDate varchar(20), # FECHA DE EXPIRACIÓN DEL TOKEN
username varchar(20), # NOMBRE DE USUARIO
password varchar(30), # CONTRASEÑA DEL USUARIO
mail varchar(45), # MAIL DEL USUARIO
attempts int, # INTENTOS DE INICIO DE SESIÓN
transactionsCount int, # TOTAL DE TRANSACCIONES REALIZADAS
mailOtp varchar(25), # CÓDIGO DE VERIFIACIÓN PARA AUTENTICAR EL INICIO DE SESIÓN POR CORREO
mailed int, # CONTADOR DE CÓDIGOS ENVIADOS AL MAIL
AccVerified bool, # BOOLEANO PARA COMPROBAR QUE LA CUENTA ESTÁ VERIFICADA
imageProfile BLOB, # IMAGEN DEL PERFIL DEL USUARIO
creationDate date, # FECHA DE CREACIÓN DE LA CUENTA
premium bool, # BOOLEANO PARA COMPROBAR SI UN USUARIO ES PREMIUM O NO
description varchar(255), # DESCRIPCION DEL USUARIO
dailyQuest date, # FECHA DE CLAIM DEL DAILY REWARD

primary key(idVault)

);

-- 2 TABLA PARA LOS USUARIOS NO REGISTRADOS
Create table if not exists UserNonRegistered (
	
    accountName varchar(20), # NOMBRE DE USAURIO
	idVault int, # ID DE LA BILLETERA
    token varchar(60), # TOKEN PARA RECONOCER UNA SESIÓN
    primary key(token)

);

-- 3 TABLA PARA LOS TOKENS DE LAS DEMO
Create table if not exists Transactions (
	
    transactionID int, # ID DE LA TRANSACCIÓN
    idVault int, # ID DE LA BILLETERA
    transactionDate date, # FECHA DE LA TRANSACCIÓN
    transactionAmount float, # CANTIDAD TRASPASADA EN LA TRANSACCIÓN
    transactionAmountFees varchar(20), # CANTIDAD TRASPASADA EN LA TRANSACCIÓN
    coinPrice float, # CANTIDAD TRASPASADA EN LA TRANSACCIÓN
    transactionCoin varchar(35), # TIPO DE MONEDA TRASPASADA EN LA TRANSACCIÓN
    transactionDestination varchar(45), # DESTINO DE LA TRANSACCIÓN
    transactionSource varchar(45), # EMITENTE DE LA TRANSACCIÓN
    transactionDescription varchar(255), # DESCRIPCIÓN DE LA TRANSACCIÓN
    benefits varchar(20), # GANANCIAS AL VENDER
    fees varchar(20), # COMISIONES
    method varchar(20), # METODO DE PAGO
    primary key(transactionID),
    foreign key(idVault) REFERENCES UserRegistered(idVault)
    

);

-- 4 TABLA PARA LAS CRYPTOS DE LOS USUARIOS DEMO
Create table if not exists DemoTransactions (
	
    transactionID int,
    -- idVaultDemo varchar(60), -- (version anterior)Falta de referencia al crear la FK
    idTokenDemo VARCHAR(60), -- Agregado
    transactionDate date,
    transactionAmount float,
    transactionCoin varchar(35),
    transactionDestination varchar(45),
    transactionSource varchar(45),
    transactionDescription varchar(255),
    demo bool,
    primary key(transactionID),
    -- foreign key(idVaultDemo) REFERENCES UserNonRegistered(idVault) -- (version anterior)Clave foranea de un atributo no de clave primaria.
	FOREIGN KEY(idTokenDemo) REFERENCES UserNonRegistered(token) -- Agregado
);
	
-- 5 TABLA PARA LAS CRYPTOS DE LOS USUARIOS
Create table if not exists UserCryptos (
	id int,
	idVault int,
    cryptoTag varchar(999),
    cryptoAmount float,
    primary key(id),
    foreign key(idVault) REFERENCES UserRegistered(idVault)

);

-- 6 TABLA PARA LAS CRYPTOS DE LOS USUARIOS DEMO
Create table if not exists DemoUserCryptos (
	id int,
	-- idVaultDemo varchar(60), -- (version anterior)Falta de referencia al crear la FK
    idTokenDemo VARCHAR(60), -- Agregado
    cryptoTag varchar(999),
    cryptoAmount varchar (999),
    primary key(id),
    -- foreign key(idVault) REFERENCES UserNonRegistered(idVault) -- (version anterior)Clave foranea de un atributo no de clave primaria
	FOREIGN KEY(idTokenDemo) REFERENCES UserNonRegistered(token) -- Agregado
);
	
-- 7 TABLA PARA LOS ADMINS
Create table if not exists UserAdmin (
	
adminID varchar(20),
password varchar(30),
mail varchar(45),
attempts int,
mailOtp varchar(25),
mailed int,
imageProfile BLOB,
creationDate date,
primary key(adminID)

);

-- 8 TABLA PARA LAS VIRTUAL CARDS
Create table if not exists VirtualCards (
	
cardID int,
cardHolder varchar(20),
digits varchar(21),
cvc int,
expDate date,
idVault int,
primary key(cardID),
foreign key (idVault) REFERENCES  UserRegistered(idVault)

);

-- 8 TABLA PARA LAS COMISIONES

Create table if not exists Commissions (

id varchar(10),
percent varchar(20),
primary key(id)

);

-- INSERTAMOS LAS COMISONES
INSERT INTO commissions VALUES("premium","1.0%");
INSERT INTO commissions VALUES("default","5.0%");

