<?php

require_once '../includes/dbOperations.php';

$response = array();

if($_SERVER['REQUEST_METHOD'] == 'GET'){
    $db = new dbOperation();
    $users = $db->getUsers();
    if(count($users) > 0){
        $response['error'] = false;
        $response['users'] = $users;
    }else{
        $response['error'] = true;
        $response['message'] = "No users found";
    }
}else{
    $response['error'] = true;
    $response['message'] = "Invalid Request";
}

echo json_encode($response);
