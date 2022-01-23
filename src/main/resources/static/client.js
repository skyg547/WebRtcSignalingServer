// var conn = new WebSocket('ws://localhost:8080/socket');
//
// function send(message) {
//     conn.send(JSON.stringify(message));
//
// }
//
//
// configuration = null;
// var peerConnection = new RTCPeerConnection(configuration);
//
//
// var dataChannel = peerConnection.createDataChannel("dataChannel", {reliable: true});


//connecting to our signaling server
var conn = new WebSocket('ws://localhost:8090/socket');

conn.onopen = function() {
    console.log("Connected to the signaling server");
    initialize();
};

conn.onmessage = function(msg) {
    console.log("Got message", msg.data);
    var content = JSON.parse(msg.data);
    var data = content.data;
    switch (content.event) {
        // when somebody wants to call us
        case "offer":
            handleOffer(data);
            break;
        case "answer":
            handleAnswer(data);
            break;
        // when a remote peer sends an ice candidate to us
        case "candidate":
            handleCandidate(data);
            break;
        default:
            break;
    }
};

function send(message) {
    conn.send(JSON.stringify(message));
}

var peerConnection;
var dataChannel;
var input = document.getElementById("messageInput");

function initialize() {
    var configuration = null;

    peerConnection = new RTCPeerConnection(configuration);

    // Setup ice handling
    peerConnection.onicecandidate = function(event) {
        if (event.candidate) {
            send({
                event : "candidate",
                data : event.candidate
            });
        }
    };

    // creating data channel
    dataChannel = peerConnection.createDataChannel("dataChannel", {
        reliable : true
    });

    dataChannel.onerror = function(error) {
        console.log("Error occured on datachannel:", error);
    };

    // when we receive a message from the other peer, printing it on the console
    dataChannel.onmessage = function(event) {
        console.log("message:", event.data);
    };

    dataChannel.onclose = function() {
        console.log("data channel is closed");
    };

    peerConnection.ondatachannel = function (event) {
        dataChannel = event.channel;
    };

}

function createOffer() {
    peerConnection.createOffer(function(offer) {
        send({
            event : "offer",
            data : offer
        });
        peerConnection.setLocalDescription(offer);
    }, function(error) {
        alert("Error creating an offer");
    });
}

function handleOffer(offer) {
    peerConnection.setRemoteDescription(new RTCSessionDescription(offer));

    // create and send an answer to an offer
    peerConnection.createAnswer(function(answer) {
        peerConnection.setLocalDescription(answer);
        send({
            event : "answer",
            data : answer
        });
    }, function(error) {
        alert("Error creating an answer");
    });

};

function handleCandidate(candidate) {
    peerConnection.addIceCandidate(new RTCIceCandidate(candidate));
};

function handleAnswer(answer) {
    peerConnection.setRemoteDescription(new RTCSessionDescription(answer));
    console.log("connection established successfully!!");
};

function sendMessage() {
    dataChannel.send(input.value);
    input.value = "";
}
//
//
// dataChannel.onerror = function (error) {
//     console.log("Error : ", error);
// };
//
// dataChannel.onclose = function () {
//     console.log("Data channel is closed");
// };
//
// peerConnection.createOffer(
//     function (offer) {
//         send({event: "offer", data: offer})
//     }, function (error) {
//
//         //Handle error here
//
//     }
// )
//
// peerConnection.onicecandidate = function (event) {
//     if (event.candidate) {
//         send({event: "candidate", data: event.candidate});
//
//     }
//
// };
//
// peerConnection.addIceCandidate(new RTCIceCandidate(candidate));
//
//
// peerConnection.setRemoteDescription(new RTCSessionDescription(offer));
//
// peerConnection.createAnswer(function (answer) {
//     peerConnection.setLocalDescription(answer);
//     send({event: "answer", data: answer});
//
//
// }, function (error) {
//     //Handle error here
//
// });
//
//
// handleAnswer(answer)
// {
//     peerConnection.setRemoteDescription(new RTCSessionDescription(answer));
//
// }
//
// dataChannel.send("message");
//
// dataChannel.onmessage = function (event) {
//     console.log("Message : ", event.data);
//
// }
//
// peerConnection.ondatachannel = function (event) {
//
//     dataChannel = event.channel;
// }
//
// const constraints = {
//     video : true,
//     audio : true
//
// };
//
// navigator.mediaDevices.getUserMedia(constraints).then(function (stream){/*use stream*/}).catch(function (error) {/*handle error */ })
//
//
// var constraints = {
//     video : {
//         frameRate : {
//             ideal : 10,
//             max : 15
//         },
//         width : 1280,
//         height : 720,
//         facingMode : "user"
//     }
// };
//
// peerConnection.addStream(stream);
//
// peerConnection.onaddstream = function(event) {
//     videoElement.srcObject = event.stream;
//
// };
//
// var configuration = {
//     "iceServers" : [ {
//         "url" : "stun:stun2.1.google.com:19302"
//     } ]
// };
//
// var configuration =  {
//     'iceServers': [
//     {
//         'urls': 'stun:stun.l.google.com:19302'
//     },
//     {
//         'urls': 'turn:10.158.29.39:3478?transport=udp',
//         'credential': 'XXXXXXXXXXXXX',
//         'username': 'XXXXXXXXXXXXXXX'
//     },
//     {
//         'urls': 'turn:10.158.29.39:3478?transport=tcp',
//         'credential': 'XXXXXXXXXXXXX',
//         'username': 'XXXXXXXXXXXXXXX'
//     }
// ]
// }