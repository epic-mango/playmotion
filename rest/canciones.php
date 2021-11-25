<?php

header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Headers: Authorization, Access-Control-Allow-Methods, Access-Control-Allow-Headers, Allow, Access-Control-Allow-Origin");
header("Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS, HEAD");
header("Allow: GET, POST, PUT, DELETE, OPTIONS, HEAD");
require_once "conexion.php";
require_once "jwt.php";
if($_SERVER["REQUEST_METHOD"]=="OPTIONS"){
    exit();
}


$h = apache_request_headers();

$metodo = $_SERVER['REQUEST_METHOD'];

if (isset($h['Authorization'])) {
    $jwt = $h['Authorization'];
    if (JWT::verify($jwt, Config::FIR) != 0) {
        //Token falso
        header("HTTP/1.1 401 Unauthorized");
        exit();
    } else {
        //Token real
        $datos = JWT::get_data($jwt, Config::FIR);
        $metodo=$_SERVER['REQUEST_METHOD'];
switch($metodo){
    case 'GET':
        $c = conexion();
        $s =$c->prepate("SELECT * FROM canciones");
        $s->execute();
        $s->setFetchMode(PDO::FETCH_ASSOC);
        $r = $s->fetchAll();
        echo json_encode($r);
    case 'POST':
        $c = conexion();
        $s = $c->prepare("INSERT INTO canciones(nombre, artista, album)VALUES(:u,:s,:v)");
        $s->bindValue(":u",$_POST['nombre']);
        $s->bindValue(":s",$_POST['artista']);
        $s->bindValue(":v",$_POST['album']);
        $s->execute();
        if($s->rowCount()){
            $id = $c->lastInsertId();
            $r = ['cancion agregada'];

        }else{
            $r = ['add' => 'no'];

        }
        echo json_encode($r);
        break;
    
}
        
    }
}else {

    header("HTTP/1.1 401 Unauthorized");
    exit();
}

