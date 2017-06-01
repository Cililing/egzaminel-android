<?php

	class DB_Connect {
		private $conn;
		
		public function connect() {
			require_once 'config.php';
			
			$this->conn = new mysqli(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);
            $this->conn->prepare("SET CHARSET utf8")->execute();
//            $this->conn->prepare("SET NAMES 'utf8' collate 'uft8_polish_ci")->execute();


			return $this->conn;
		}
	}
	
?>