<?php

$_POST['groups_ids'] = json_encode(array(1));

require_once 'include/DB_Functions.php';
$db = new DB_Functions();

$groups_ids = json_decode($_POST['groups_ids']);
$error = array("error" => true);

if (isset($_POST['groups_ids'])) {

    $response = $db->getExamsAndTermsByGroupsIDs($groups_ids);
    echo $response;
}


else {
    echo json_encode($error);
}

?>