<?php

require_once "include/DB_SimpleLogin.php";
$db_login = new DB_SimpleLogin();

$error = array("error" => true);

if (isset($_POST['username']) AND isset($_POST['password'])) {

    $username = $_POST['username'];
    $password = $_POST['password'];

    $result = $db_login->login($username, $password);
    echo $result;
}

else {
    echo json_encode($error);
}

?>