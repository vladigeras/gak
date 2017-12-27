import {Component} from '@angular/core';
import {Http} from "@angular/http";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'test frontend part of application';

  constructor(private http: Http) {
  }

  ngOnInit() {
    this.http.get("/api/hello").subscribe(data => {
      this.title = data.text() + "dsfshhvhjdf";
    });
  }
}

