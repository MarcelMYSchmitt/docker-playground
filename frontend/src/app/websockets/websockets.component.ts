import { Component, OnInit, OnDestroy } from '@angular/core';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';

@Component({
  selector: 'app-websockets',
  templateUrl: './websockets.component.html',
  styleUrls: ['./websockets.component.css']
})
export class WebsocketsComponent implements OnInit, OnDestroy {

  webSocketEndPoint: string = 'http://localhost:8380/ws';
  topic: string = "/topic/messages";
  stompClient: any;

  rawMessage: any;
  messageRandomId: any;
  messageMessage: any;
  messageTimestamp: any;
  
  constructor() { }

  ngOnInit() {
    this.connect();
  }

  ngOnDestroy() {
    this.disconnect();
  }

  connect() {
    console.log("Initialize WebSocket Connection");
    let ws = new SockJS(this.webSocketEndPoint);
    this.stompClient = Stomp.over(ws);
    const _this = this;
    _this.stompClient.connect({}, function (frame) {
      _this.stompClient.subscribe(_this.topic, function (sdkEvent) {
        _this.onMessageReceived(sdkEvent);
      });
      //_this.stompClient.reconnect_delay = 2000;
    }, this.errorCallBack);
  };

  disconnect() {
    if (this.stompClient !== null) {
      this.stompClient.disconnect();
    }
    console.log("Disconnected");
  }

  onMessageReceived(message) {
    console.log("Message Recieved from Server :: " + message);
    this.rawMessage = message;

    let messageJsonString = JSON.stringify(message.body);
    let rawMessageJsonObject = JSON.parse(messageJsonString);


    let webSocketMessage: WebSocketMessage = new WebSocketMessage(rawMessageJsonObject);

    this.messageRandomId = webSocketMessage.randomId;
    this.messageMessage = webSocketMessage.message;
    this.messageTimestamp = webSocketMessage.timestamp;

  }

  // on error, schedule a reconnection attempt
  errorCallBack(error) {
    console.log("errorCallBack -> " + error)
    setTimeout(() => {
      this.connect();
    }, 5000);
  }

}

class WebSocketMessage {
  randomId: number;
  message: string;
  timestamp: number;

  constructor(jsonStr: string) {
    let jsonObj: any = JSON.parse(jsonStr);
    for (let prop in jsonObj) {
      this[prop] = jsonObj[prop];
    }
  }
}