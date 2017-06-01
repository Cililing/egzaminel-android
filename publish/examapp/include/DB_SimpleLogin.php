<?php

class DB_SimpleLogin {

    private $conn;

    function __construct() {
        require_once 'DB_Connect.php';
        $db = new DB_connect();
        $this->conn = $db->connect();
    }

    function __destruct() {
    }

    function login($username, $password) {
        $stmt = $this->conn->prepare
        ('SELECT users.id FROM users WHERE username = ? AND password = ?');
        $stmt->bind_param("ss", $username, $password);
        $stmt->execute();
        $data = $stmt->get_result();
        $result = array();
        $result['error'] = true;
        if ($data->num_rows > 0) {
            //user exists, return his id
            $result['error'] = false;
            $result['result'] = $data->fetch_assoc();
        }
        return json_encode($result);
    }

}

?>