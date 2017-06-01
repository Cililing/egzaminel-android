<?php

require_once 'include/DB_Functions.php';
$db = new DB_Functions();

$error = array("error" => true);

if (isset($_POST['user_id'])) {

    $exam_id = $_POST['user_id'];
    $response = $db->getTermsByUserId($exam_id);
    echo $response;
}


else {
    echo json_encode($error);
}

?>