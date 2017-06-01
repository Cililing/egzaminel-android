<?php

    class DB_Login {

        private $conn;

        function __construct() {
            require_once 'DB_Connect.php';
            $db = new DB_connect();
            $this->conn = $db->connect();
        }

        function __destruct() {
        }


        public function storeUser($name, $password) {
            $hash = $this->hashSSHA($password);
            $encrypted_password = $hash["encrypted"];
            $salt = $hash["salt"];

            $stmt = $this->conn->prepare
                ('INSERT INTO users(username, password, salt) VALUES (?, ?, ?)');

            $stmt->bind_param("ssss", $name, $encrypted_password, $salt);
            $result = $stmt->execute();
            $stmt->close();

            //check if res is stored
            if ($result) {
                $stmt = $this->conn->prepare
                    ('SELECT id FROM users WHERE username = ?');
                $stmt->bind_param('s', $name);
                $stmt->execute();
                $user = $stmt->get_result()->fetch_assoc();
                $stmt->close();

                return $user;
            } else {
                return false;
            }

        }

        public function getUserByIdAndPassword($username, $password) {
            $stmt = $this->conn->prepare
                ('SELECT * FROM users WHERE username = ?');
            $stmt->bind_param("s", $username);
            if ($stmt->execute()) {
                $user = $stmt->get_result()->fetch_assoc();
                $stmt->close();

                //veryfying password
                $salt = $user['salt'];
                $encrypted_password = $user['password'];
                $hash = $this->checkhashSSHA($salt, $password);

                if ($encrypted_password == $hash) {
                    return $user;
                }
            } else {
                return NULL;
            }
        }

        public function isUserExisted($username) {
            $stmt = $this->conn->prepare
                ('SELECT username FROM users WHERE username = ?');
            $stmt->bind_param("s", $username);
            $stmt->execute();
            $stmt->store_result();

            if ($stmt->num_rows > 0) {
                //user exist
                $stmt->close();
                return true;
            } else {
                $stmt->close();
                return false;
            }
        }

        /**Encrypting password
         * @param $password
         * @return array
         */
        private function hashSSHA($password) {
            $salt = sha1(rand());
            $salt = substr($salt, 0, 10);
            $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
            $hash = array("salt" => $salt, "encrypted" => $encrypted);
            return $hash;
        }

        /**Decrypting password
         * @param $salt
         * @param $password
         * @return string
         */
        public function checkhashSSHA($salt, $password) {
            $hash = base64_encode(sha1($password . $salt, true) . $salt);
            return $hash;
        }
    }


?>