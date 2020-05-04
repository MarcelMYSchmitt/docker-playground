import { Component, OnInit, AfterViewInit } from '@angular/core';
import { MatomoInjector } from 'ngx-matomo'
import { Observable, from, throwError } from 'rxjs';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { tap } from 'rxjs/operators';
import { EMPTY } from 'rxjs';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent {

  private matomoUrl = "http://localhost:7000";

  constructor(private matomoInjector: MatomoInjector, private httpClient: HttpClient) {
  }

  ngOnInit() {
    this.httpClient.get(this.matomoUrl)
      .subscribe(
        data => this.InitializeConnectionToMatomo(),
        err => this.checkError(err),
        () => console.log('yay....')
      );
  }

  InitializeConnectionToMatomo() {
    console.log("Establish connection to Matomo...");
    this.matomoInjector.init(this.matomoUrl, 1);
  }

  checkError(error: HttpErrorResponse) {
    if (error.status == 200) {
      this.InitializeConnectionToMatomo();
    } else {
      console.log("Matomo via " + this.matomoUrl + " not available. Is it running?")
    }
  }
}