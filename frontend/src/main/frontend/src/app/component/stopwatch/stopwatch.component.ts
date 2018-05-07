import { Component } from '@angular/core';
import {StopWatchService} from "../../service/stopwatch.service";



@Component({
  selector: 'app-stopwatch',
  templateUrl:'./stopwatch.component.html',
  styleUrls: ['./stopwatch.component.scss']
})

export class StopWatchComponent {
  public started: boolean = true;
  //public stopwatchService: StopWatchService;
  public time: number;

  public autoStart: boolean = true;

  private timer: any;

  constructor(private stopwatchService: StopWatchService) {
    this.time = 0;
    this.started = true;
    this.update();
    if (this.autoStart) {
      this.start();
    }
  }

  formatTime(timeMs: number) {
    let minutes: string,
      seconds: string;

    minutes = Math.floor(timeMs / 60000).toString();
    seconds = ((timeMs % 60000) / 1000).toFixed(3);
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
  }

  reset() {
    this.stopwatchService.reset();
    this.started = false;
    this.update();
  }

  start() {
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
}



