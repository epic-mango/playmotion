<?php


header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Headers: Authorization, Access-Control-Allow-Methods, Access-Control-Allow-Headers, Allow, Access-Control-Allow-Origin");
header("Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS, HEAD");
header("Allow: GET, POST, PUT, DELETE, OPTIONS, HEAD");
require_once "conexion.php";
require_once "jwt.php";


if ($_SERVER["REQUEST_METHOD"] == "OPTIONS") {
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
        if ($metodo == 'PUT') {
            //Cambiar datos usuario
        }
    }
} else {



    switch ($metodo) {
        case 'GET':
            //login
            if (isset($_GET['usuario']) && isset($_GET['contrasenia'])) {
                $c = conexion();
                $s = $c->prepare("SELECT * FROM usuarios WHERE usuario=:u AND contrasenia=sha1(:p)");
                $s->bindValue(":u", $_GET['usuario']);
                $s->bindValue(":p", $_GET['contrasenia']);
                $s->execute();
                $s->setFetchMode(PDO::FETCH_ASSOC);
                $r = $s->fetchAll();
                if ($r) {
                    $jwt = JWT::create(['usuario' => $_GET['usuario']], Config::FIR, 7200);
                    $m = array('login' => 'yes', 'jwt' => $jwt);
                } else {
                    $m = ['login' => 'no'];
                }
                header("HTTP/1.1 200 OK");
                echo json_encode($m);
            } else {
                header("HTTP/1.1 400 Bad Request");
            }
            break;
        case 'POST':
        $c = conexion();
        $s = $c->prepare("INSERT INTO usuarios(usuario, nombre, contrasenia, fechanacimiento)VALUES(:u,:s, sha1(:v),:f)");
        $s->bindValue(":u",$_POST['usuario']);
        $s->bindValue(":s",$_POST['nombre']);
        $s->bindValue(":v",$_POST['contrasenia']);
        $s->bindValue(":f",$_POST['fechanacimiento']);
        $s->execute();
        if($s->rowCount()){
            $jwt = JWT::create(['usuario' => $_POST['usuario']], Config::FIR, 7200);
            $m = array('signup' => 'yes', 'jwt' => $jwt);
        echo json_encode($m);

        }else{
            $r = ['signup' => 'no'];
            echo json_encode($r);
        }
            break;


        default:
            header("HTTP/1.1 400 Bad Request");
    }
}
