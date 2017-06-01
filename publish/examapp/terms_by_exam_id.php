<?php

require_once 'include/DB_Functions.php';
$db = new DB_Functions();

$error = array("error" => true);

if (isset($_POST['exam_id'])) {

    $exam_id = $_POST['exam_id'];
    $response = $db->getExamTerms($exam_id);
    echo $response;
}


else {
    echo json_encode($error);
}

?>