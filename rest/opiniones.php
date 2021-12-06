<?php


function array_sort($array, $on, $order = SORT_ASC)
{
    $new_array = array();
    $sortable_array = array();

    if (count($array) > 0) {
        foreach ($array as $k => $v) {
            if (is_array($v)) {
                foreach ($v as $k2 => $v2) {
                    if ($k2 == $on) {
                        $sortable_array[$k] = $v2;
                    }
                }
            } else {
                $sortable_array[$k] = $v;
            }
        }

        switch ($order) {
            case SORT_ASC:
                asort($sortable_array);
                break;
            case SORT_DESC:
                arsort($sortable_array);
                break;
        }

        foreach ($sortable_array as $k => $v) {
            $new_array[$k] = $array[$k];
        }
    }

    return $new_array;
}

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
        $metodo = $_SERVER['REQUEST_METHOD'];
        switch ($metodo) {
            case 'GET':
                $lista = [];

                if (isset($_GET['emocion']) && isset($_GET['cantidad'])) {
                    $c = conexion();
                    $s = $c->prepare('SELECT id FROM canciones');
                    $s->execute();
                    $s->setFetchMode(PDO::FETCH_NUM);

                    while ($fila = $s->fetch()) {

                        //total de todas las relaciones
                        $c = conexion();
                        $s2 = $c->prepare('SELECT count(*) AS Total FROM relaciones');
                        $s2->execute();
                        $s2->setFetchMode(PDO::FETCH_ASSOC);
                        $r = $s2->fetch();
                        $total = $r['Total'];

                        //Saber cuantas coinciden con la emocion que pidio el usuario
                        $s2 = $c->prepare("SELECT count(*) AS Total FROM relaciones WHERE idemocion=:e");
                        $s2->bindValue(':e', $_GET['emocion']);
                        $s2->execute();
                        $s2->setFetchMode(PDO::FETCH_ASSOC);
                        $r = $s2->fetch();
                        $totalEmociones = $r['Total'];

                        $s2 = $c->prepare("SELECT count(*) AS Total FROM relaciones WHERE idemocion=:e
             AND idcancion=$fila[0]");
                        $s2->bindValue(':e', $_GET['emocion']);
                        $s2->execute();
                        $s2->setFetchMode(PDO::FETCH_ASSOC);
                        $r = $s2->fetch();
                        $totalEmocancion = $r['Total'];

                        $s2 = $c->prepare("SELECT count(*) AS Total FROM relaciones WHERE idemocion=:e 
            AND usuario=:u");
                        $s2->bindValue(':e', $_GET['emocion']);
                        $s2->bindValue(':u', $datos['usuario']);
                        $s2->execute();
                        $s2->setFetchMode(PDO::FETCH_ASSOC);
                        $r = $s2->fetch();
                        $totalEmoUsuario = $r['Total'];

                        $emo = $totalEmociones / $total;

                        $temo = 1;
                        if ($totalEmociones != 0) {
                            if ($totalEmocancion != 0) $temo *= ($totalEmocancion / $totalEmociones);
                            else $temo *= 1 / $total;


                            if ($totalEmoUsuario != 0) $temo *= ($totalEmoUsuario / $totalEmociones);
                            else $temo *= 1 / $total;

                            if ($temo==1) $temo = 1/$total;
                        }


                        $porcentaje = ($temo * $emo) * 100;

                        array_push($lista, ['id' => $fila[0], 'porcentaje' => $porcentaje]);
                    }

                    $lista = array_sort($lista, 'porcentaje', SORT_DESC);

                    $respuesta = [];
                    $cont = 0;
                    foreach ($lista as $cancion) {


                        $c = conexion();
                        $s = $c->prepare("SELECT * FROM canciones WHERE id = :id");
                        $s->bindValue(":id", $cancion['id']);
                        $s->execute();
                        $s->setFetchMode(PDO::FETCH_ASSOC);
                        $r = $s->fetchAll();

                        $r[0]['porcentaje'] = $cancion['porcentaje'];

                        array_push($respuesta, $r);

                        $cont++;
                        if ($cont == $_GET['cantidad']) break;
                    }




                    //$r = ['total' => $total, 'totalEmociones' => $totalEmociones, 'totalEmocancion' => $totalEmocancion,
                    //   'totalEmousuario' => $totalEmoUsuario];
                } else {
                    header("HTTP/1.1 400 Bad Request");
                }

                echo json_encode($respuesta);

                break;
            case 'POST':
                $c = conexion();
                $s = $c->prepare("SELECT usuario FROM relaciones WHERE idcancion=:c AND usuario=:o");
                $s->bindValue(":c", $_POST['idcancion']);
                $s->bindValue(":o", $datos['usuario']);
                $s->execute();
                $r = $s->fetchAll();
                if ($r) {

                    $s = $c->prepare("UPDATE relaciones SET idemocion=:e WHERE idcancion=:c AND usuario=:o");
                    $s->bindValue(":c", $_POST['idcancion']);
                    $s->bindValue(":e", $_POST['idemocion']);
                    $s->bindValue(":o", $datos['usuario']);
                    $s->execute();
                    //regresar al usuario un aviso de que se logro hacer update
                    $r = ['add' => 'yes', 'tipo' => 'actualizar'];


                    // $jwt = JWT::create(['idemocion' => $_GET['idemocion']], Config::FIR, 7200);
                    // $m = array('login' => 'yes', 'jwt' => $jwt);

                } else {
                    $s = $c->prepare("INSERT INTO relaciones(usuario, idcancion, idemocion) VALUES (:o,:c,:e)");
                    $s->bindValue(":c", $_POST['idcancion']);
                    $s->bindValue(":e", $_POST['idemocion']);
                    $s->bindValue(":o", $datos['usuario']);
                    $s->execute();
                    $r = ['add' => 'yes', 'tipo' => 'agregar'];
                    //Hacer un aviso de que se añadió correctamente la opinión
                    // $m = ['agregaropinion' => 'no']
                }
                echo json_encode($r);

                break;
        }
    }
} else {

    header("HTTP/1.1 401 Unauthorized");
    exit();
}
