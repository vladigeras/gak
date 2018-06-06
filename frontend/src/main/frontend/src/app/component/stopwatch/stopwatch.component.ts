import {Component, EventEmitter, Output} from '@angular/core';
import {StopWatchService} from "../../service/stopwatch.service";
import {BlockUI, NgBlockUI} from "ng-block-ui";
import {currentPrincipal} from "../../security/auth.service";
import {ToastsManager} from "ng2-toastr";
import {SocketService} from "../../service/socket.service";
import {CriteriaService} from "../../service/criteria.service";
import {waitString} from "../../app.module";
import {Status} from "../../status";
import {TimestampService} from "../../service/timestamp.service";



@Component({
  selector: 'app-stopwatch',
  templateUrl:'./stopwatch.component.html',
  styleUrls: ['./stopwatch.component.scss']
})

export class StopWatchComponent {

  @Output()
  flagLabs: EventEmitter<number> = new EventEmitter<number>();

  public started: boolean = true;
  //public stopwatchService: StopWatchService;
  public time: number;


  public autoStart: boolean = false;
  private timer: any;
  private flagStartDefense = false;
  private countLaps = 0;

  principal = currentPrincipal;
  activeSpeaker = {id: null, fio: null, date:null};
  timestamps = [];

  @BlockUI() blockUI: NgBlockUI;

  constructor(private stopwatchService: StopWatchService,
              private toast: ToastsManager,
              private socketService: SocketService,
              private timestampService: TimestampService) {
    socketService.activeSpeakerReady.subscribe(speaker => {
      if (speaker != null) {
        this.activeSpeaker = {
          id: speaker.id,
          fio: speaker.student.lastname + " " + speaker.student.firstname + " " + speaker.student.middlename,
          date: speaker.data
        };
      }
    });
    this.time = 0;
    this.started = false;
    this.flagStartDefense = false;
    this.update();
    if (this.autoStart) {
      this.start();
    }
  }



  formatTime(timeMs: number) {
    let minutes: string,
      seconds: string;

    minutes = Math.floor(timeMs / 60000).toString();
    seconds = ((timeMs % 60000) / 1000).toFixed(0);
    return minutes + ':' + (+seconds < 10 ? '0' : '') + seconds;
  }

  getUpdate() {
    let self = this;

    return () => {
      self.time = this.stopwatchService.time();
    };
  }

  lap() {
    this.update();

    if (this.time) {
      this.stopwatchService.lap();
    }
    this.countLaps++;

  }

  reset() {
    this.stopwatchService.reset();
    this.started = false;
    this.flagStartDefense = false;
    this.countLaps = 0;
    this.update();
  }

  start() {
    this.flagStartDefense = true;
    this.timer = setInterval(this.getUpdate(), 1);
    this.stopwatchService.start();
  }

  stop() {
    clearInterval(this.timer);
    this.stopwatchService.stop();
  }

  toggle() {
    if (this.started) {
      this.stop();
    } else {
      this.start();
    }

    this.started = !this.started;
  }

  update() {
    this.time = this.stopwatchService.time();
  }

  onClick() {
    console.log(this.stopwatchService);
  }
  lapAndStop(){
    this.update();

    if (this.time) {
      this.stopwatchService.lap();
    }
    this.countLaps++;

    clearInterval(this.timer);
    this.stopwatchService.stop();
    let laps = this.stopwatchService.getLaps();
    laps.forEach((l, index)  =>{
        if (index == 1)  {
          this.timestamps.push({status: Status.SPEAKING_TIME, timestamp: l.startMs})
          this.timestamps.push({status: Status.SPEAKING_TIME_END, timestamp: l.endMs})
        }

        if (index == 2)  this.timestamps.push({status: Status.REWIEW_TIME_END, timestamp: l.endMs})
        if (index == 3)  this.timestamps.push({status: Status.QUESTION_TIME_END, timestamp: l.endMs})
        if (index == 4)  this.timestamps.push({status: Status.ALL_TIME, timestamp: l.endMs})

    }

    );
  }students

  getDataToSpeakersStudentTable(){
   this.flagLabs.emit(4);
   this.reset();
  }

  saveTimestamp() {
    this.getDataToSpeakersStudentTable();
      this.blockUI.start(waitString);
      console.log(this.timestamps);
      this.timestampService.saveTimestamp(this.activeSpeaker.id, this.timestamps).subscribe(
        data => {
          this.blockUI.stop();
          this.toast.success("Сохранено", "Успешно")
        },
        error => {
          this.blockUI.stop()
        }
      );
      this.timestamps = [];

  }

}



