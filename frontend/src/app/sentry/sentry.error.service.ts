import { Injectable, ErrorHandler, Injector } from '@angular/core';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http'
import * as Sentry from "@sentry/browser";

Sentry.init({
  dsn: "http://257eaf0638044d80938e526375970d70@localhost:9000/2",
  release: "playground-1.0"
});

@Injectable({
  providedIn: 'root'
})

export class SentryErrorHandler implements ErrorHandler {
  constructor(private injector: Injector) { }
  handleError(error: any) {
    const router = this.injector.get(Router);
    //capture error to sentry cloud
    const eventId = Sentry.captureException(error.originalError || error);
    if (Error instanceof HttpErrorResponse) {
    console.log(error.status);
    }
    else {
    console.error("an error occured here mate");
    //ask user to report error if error not server related
    Sentry.showReportDialog({ eventId });
    }
    //navigate to error page
    router.navigate(['error']);
    
    //return error;
    //pass the error if needed
  }
}