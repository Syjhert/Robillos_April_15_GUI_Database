<?php
include 'connect.php';
require_once 'includes/header.php';

if(session_status() !== PHP_SESSION_ACTIVE) session_start();

function showMessage($message) {
  echo "
      <div id='message-box'>
        <p>$message</p>
      </div>
    ";

  echo "
      <script>
        setTimeout(function() {
          var messageBox = document.getElementById('message-box');
          if (messageBox) {
            messageBox.parentNode.removeChild(messageBox);
          }
        }, 3000);
      </script>
    ";
}
?>

<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" href="css/message.css" />
  <link rel="stylesheet" href="css/nav.css" />
  <link rel="stylesheet" href="css/logged_in.css" />
  <title>Accord</title>
  <script>
    if ( window.history.replaceState ) {
        window.history.replaceState( null, null, window.location.href );
    }

    function deleteOnClick(messageID){
      showMessage("Deleted Message");
      <?php
        $sqlDeleteMessage = "DELETE FROM tblmessage WHERE messageID='<script>document.write(messageID)</script>'";
        mysqli_query($connection, $sqlDeleteMessage);
      ?>
    }
  </script>
</head>

<body>


  <div id="loggedin-cont">
    <?php
    echo "
        <div>
          <h1>Hello {$_SESSION['username']}</h1>
        </div>
      ";

      $sqlGetAllMessage = "SELECT * FROM tblmessage";
      $resultAllMessage = mysqli_query($connection, $sqlGetAllMessage);
    ?>

    <form method="post">
      <div>
      <textarea id="txtMessage" name="txtMessage" placeholder="Your Message"></textarea>
        <input type="submit" name="btnSend" value="Send">
      </div>
    </form>

    <table id="tblmessage">
      <thead>
          <tr>
            <th>Message ID</th>
            <th>Sender ID</th>
            <th>Channel ID</th>
            <th>Message Text</th>
            <th>Date Time Sent</th>
            <th>ACTION</th>
          </tr>
      </thead>
      <tbody>
        <?php
          while($row = mysqli_fetch_array($resultAllMessage)){
            echo "<tr>";
              echo "<td>" . $row['messageID'] . "</td>";
              echo "<td>" . $row['senderID'] . "</td>";
              echo "<td>" . $row['channelID'] . "</td>";
              echo "<td>" . $row['messageText'] . "</td>";
              echo "<td>" . $row['dateTimeSent'] . "</td>";
              echo "<td>";
                echo "<a href=''>VIEW</a>";
                echo "<a href='delete.php?id=".$row['messageID']."&uid=".$_SESSION['userid']."&uname=".$_SESSION['username']."'>DELETE</a>";
              echo "</td>";
            echo "</tr>";
          }
          // endwhile;?>
      </tbody>
    </table>

  </div>

</body>

</html>

<?php

if (isset($_POST['btnSend'])) {
  $senderID = $_SESSION['userid'];
  $channelID = 1;
  $messageText = $_POST['txtMessage'];
  $dateTimeSent = date("Y/m/d") ."-" . date("h:i:sa");

  $sqlInsertMessage = "INSERT INTO tblmessage(senderID,channelID,messageText,dateTimeSent) VALUES('". $senderID ."', '". $channelID ."', '". $messageText ."', '". $dateTimeSent ."')";
  mysqli_query($connection, $sqlInsertMessage);
  showMessage("Message Sent");
}


?>