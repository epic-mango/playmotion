<?php
//pegar aqui el codigo que mando el profe 

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
       //bloque de seguridad
       $jwt = $h['Authorization'];
       if(JWT::verify($jwt, Config::FIR)!=0){
       header("HTTP/1.1 401 Unauthorized");
        exit();
       }
       $datos=JWT::get_data($jwt,Config::FIR);
    
//aqui se acaba le bloque de seguridad

$metodo = $_SERVER['REQUEST_METHOD'];
switch($metodo){
    case 'GET':
        if(isset($_GET['usuario']) && isset($_GET['contrasenia'])){
            $c = conexion();
            $s = $c->prepare("SELECT * FROM usuarios WHERE usuario=:u AND contrasenia=sha1(:p)");
            $s->bindValue(":u", $_GET['usuario']);
            $s->bindValue(":p", $_GET['contrasenia']);
            $s->execute();
            $s->setFetchMode(PDO::FETCH_ASSOC);
            $r = $s->fetchAll();
            if($r){
                $jwt = JWT::create(['usuario'=>$_GET['usuario']], Config::FIR,7200);
                $m = array('login'=>'yes', 'jwt'=>$jwt);
            }else{
                $m = ['login'=>'no'];
            }
            header("HTTP/1.1 200 OK");
            echo json_encode($m);
        }else{
            header("HTTP/1.1 400 Bad Request");
        }
        break;

        
    default:
        header("HTTP/1.1 400 Bad Request");
}
