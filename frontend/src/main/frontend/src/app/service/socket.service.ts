import {EventEmitter, Injectable} from "@angular/core";
import {socketPath} from "../app.module";
import Stomp from 'stompjs';
import SockJS from 'sockjs-client';
import {ToastsManager} from "ng2-toastr";

@Injectable()
export class SocketService {

  constructor(private toast: ToastsManager) {}

  socketReady = new EventEmitter();
  activeSpeakerReady = new EventEmitter();
  doneSpeakerReady = new EventEmitter();

  connect() {
    websocket = new SockJS(socketPath);
    stomp = Stomp.over(websocket);

    stomp.connect({}, () => {
      this.socketReady.emit(stomp);

      stomp.subscribe("/active", (activeSpeakerString: any) => {
        if (activeSpeakerString.body) {
          let activeSpeaker = JSON.parse(activeSpeakerString.body);
          this.activeSpeakerReady.emit(activeSpeaker);
          this.toast.success("Секретарь выбрал следующего студента для защиты");
        }
      });

      stomp.subscribe("/done", (doneSpeakerString: any) => {
        if (doneSpeakerString.body) {
          let doneSpeaker = JSON.parse(doneSpeakerString.body);
          this.doneSpeakerReady.emit(doneSpeaker);
          this.toast.success("Председатель окончил защиту для выбранного студента");
        }
      });
    });
  }
}

export let websocket = null;
export let stomp = null;

