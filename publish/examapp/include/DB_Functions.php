<?php

class DB_Functions {

    private $conn;

    function __construct() {
        require_once 'DB_Connect.php';
        $db = new DB_connect();
        $this->conn = $db->connect();
    }

    function __destruct() {
    }

    public function getUserGroupsById($user_id) {
        $stmt = $this->conn->prepare
        ('SELECT groups.id, groups.name, groups.description, groups.last_update
                  FROM groups
                  JOIN groups_members ON groups_members.group_id = groups.id
                  WHERE groups_members.user_id = ?'

        );
        $stmt->bind_param("i", $user_id);
        $stmt->execute();
        $data = $stmt->get_result();
        $res_arr = array();
        $res_arr['error'] = true;
        while ($row = $data->fetch_assoc()) {
            $res_arr['error'] = false;
            $res_arr['result'][] = $row;
        }
        $result = json_encode($res_arr);
        return $result;
    }

    public function getExamsByGroup($group_id) {
        $stmt = $this->conn->prepare
        ('SELECT *
                FROM exams
                WHERE group_id = ?
                GROUP BY exams.id'
        );
        $stmt->bind_param("i", $group_id);
        $stmt->execute();
        $data = $stmt->get_result();
        $res_arr = array();
        $res_arr['error'] = true;
        while ($row = $data->fetch_assoc()) {
            $res_arr['error'] = false;
            $res_arr['result'][] = $row;
        }
        $result = json_encode($res_arr);
        return $result;
    }

    public function getExamsByUserID($user_id) {
        $stmt = $this->conn->prepare
        ('SELECT *
                  FROM exams
                  WHERE exams.group_id IN
                  (
	                SELECT groups_members.group_id
                    FROM groups_members
                    WHERE groups_members.user_id = ?
                  )'
        );
        $stmt->bind_param("i", $user_id);
        $stmt->execute();
        $data = $stmt->get_result();
        $res_arr = array();
        $res_arr['error'] = true;
        while ($row = $data->fetch_assoc()) {
            $res_arr['error'] = false;
            $res_arr['result'][] = $row;
        }
        $result = json_encode($res_arr);
        return $result;
    }

    public function getExamTerms($exam_id) {
        $stmt = $this->conn->prepare
        ('SELECT *
                FROM terms
                WHERE terms.exam_id = ?
                GROUP BY terms.id'
        );
        $stmt->bind_param("i", $exam_id);
        $stmt->execute();
        $data = $stmt->get_result();
        $res_arr = array();
        $res_arr['error'] = true;
        while ($row = $data->fetch_assoc()) {
            $res_arr['error'] = false;
            $res_arr['result'][] = $row;
        }
        $result = json_encode($res_arr);
        return $result;
    }

    public function getTermsByUserId($user_id) {
        $stmt = $this->conn->prepare
        ('SELECT *
                  FROM terms
                  WHERE terms.exam_id IN 
                  (
                    SELECT exams.id
                    FROM exams
                    WHERE exams.group_id IN
                    (
                      SELECT groups_members.group_id
                      FROM groups_members
                      WHERE groups_members.user_id = ?
                    )
                  )'
        );
        $stmt->bind_param("i", $user_id);
        $stmt->execute();
        $data = $stmt->get_result();
        $res_arr = array();
        $res_arr['error'] = true;
        while ($row = $data->fetch_assoc()) {
            $res_arr['error'] = false;
            $res_arr['result'][] = $row;
        }
        $result = json_encode($res_arr);
        return $result;
    }

    public function getGroupByIdAndPassword($group_id, $password) {
        $stmt = $this->conn->prepare
        ("
          SELECT *
          FROM groups
          WHERE groups.id = ? AND groups.password = ?
          GROUP BY groups.id        
        ");
        $stmt->bind_param("is", $group_id, $password);
        $stmt->execute();
        $data = $stmt->get_result();
        $res_arr['error'] = true;
        while ($row = $data->fetch_assoc()) {
            $res_arr['error'] = false;
            $res_arr['result'][] = $row;
        }
        $result = json_encode($res_arr);
        return $result;
    }

    public function getExamsAndTermsByGroupsIDs($group_ids) {

        //exams
        $qMarks = implode(',', $group_ids);
        $stmt = $this->conn->prepare
        ("
            SELECT *
            FROM exams
            WHERE exams.group_id IN ($qMarks)
            GROUP BY exams.id
        ");

        $stmt->execute();
        $data = $stmt->get_result();
        $res_arr['error'] = true;
        while ($row = $data->fetch_assoc()) {
            $res_arr['error'] = false;
            $res_arr['result_exams'][] = $row;
        }

        //terms
        $stmtT = $this->conn->prepare
        ("
                SELECT *
                FROM terms
                WHERE terms.exam_id IN 
                  (
                    SELECT exams.id
                    FROM exams
                    WHERE exams.group_id IN ($qMarks)
                  )
        ");

        $stmtT->execute();
        $dataT = $stmtT->get_result();
        $res_arr['error'] = true;
        while ($rowT = $dataT->fetch_assoc()) {
            $res_arr['error'] = false;
            $res_arr['result_terms'][] = $rowT;
        }

        $result = json_encode($res_arr);
        return $result;
    }
}