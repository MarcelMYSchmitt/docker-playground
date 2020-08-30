import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-sentry',
  templateUrl: './sentry.component.html',
  styleUrls: ['./sentry.component.css']
})
export class SentryComponent {

  errorText = "";

  whatHappensOnClickError() {
    let errorMessage ="This is a test error for Sentry."
    this.errorText = errorMessage;

    throw new Error();
  }

}
