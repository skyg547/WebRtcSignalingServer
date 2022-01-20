var conn = new WebSocket('ws://localhost:8080/socket');

function send(message) {
    conn.send(JSON.stringify(message));

}


configuration = null;
var peerConnection = new RTCPeerConnection(configuration);


var dataChannel = peerConnection.createDataChannel("dataChannel", {reliable: true});


dataChannel.onerror = function (error) {
    console.log("Error : ", error);
};

dataChannel.onclose = function () {
    console.log("Data channel is closed");
};

peerConnection.createOffer(
    function (offer) {
        send({event: "offer", data: offer})
    }, function (error) {

        //Handle error here

    }
)

peerConnection.onicecandidate = function (event) {
    if (event.candidate) {
        send({event: "candidate", data: event.candidate});

    }

};

peerConnection.addIceCandidate(new RTCIceCandidate(candidate));


peerConnection.setRemoteDescription(new RTCSessionDescription(offer));

peerConnection.createAnswer(function (answer) {
    peerConnection.setLocalDescription(answer);
    send({event: "answer", data: answer});


}, function (error) {
    //Handle error here

});


handleAnswer(answer)
{
    peerConnection.setRemoteDescription(new RTCSessionDescription(answer));

}

dataChannel.send("message");

dataChannel.onmessage = function (event) {
    console.log("Message : ", event.data);

}

peerConnection.ondatachannel = function (event) {

    dataChannel = event.channel;
}