<?php

require_once 'include/DB_Functions.php';
$db = new DB_Functions();

$error = array("error" => true);

if (isset($_POST['group_id']) && isset($_POST['password'])) {

    $group_id = $_POST['group_id'];
    $password = $_POST['password'];
    $response = $db->getGroupByIdAndPassword($group_id, $password);
    echo $response;
}

else {
    echo json_encode($error);
}

?>