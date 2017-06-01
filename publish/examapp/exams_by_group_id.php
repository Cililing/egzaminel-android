<?php

require_once 'include/DB_Functions.php';
$db = new DB_Functions();

$error = array("error" => true);

if (isset($_POST['group_id'])) {

    $group_id = $_POST['group_id'];
    $response = $db->getExamsByGroup($group_id);
    echo $response;
}


else {
    echo json_encode($error);
}

?>