import { Component, OnInit, AfterViewInit } from '@angular/core';
import { MatomoTracker } from 'ngx-matomo';

@Component({
  selector: 'app-matomo',
  templateUrl: './matomo.component.html',
  styleUrls: ['./matomo.component.css']
})

export class MatomoComponent implements OnInit, AfterViewInit {

  constructor(private matomoTracker: MatomoTracker) {

  }

  whatHappensOnClickLeft(someVal){
    console.log("Button Left Clicked with value:"+ someVal)
    this.matomoTracker.trackEvent('Button', 'Clicked', 'Left', someVal);
  }

  whatHappensOnClickRight(someVal){
    console.log("Button Right Clicked with value:"+ someVal)
    this.matomoTracker.trackEvent('Button', 'Clicked', 'Right', someVal);
  }

  ngOnInit() {
    // user id should not be hardcoded
    this.matomoTracker.setUserId('1234');
    //this.matomoTracker.setDocumentTitle('Matomo Test Page');
  }

  /**
   * AfterViewInit lifecycle hook
   */
  ngAfterViewInit() {
    this.matomoTracker.trackPageView('Matomo Test Page');
    // track event could have a counter 
    this.matomoTracker.trackEvent('Visit', 'View', 'MatomoPage', 1);

    this.matomoTracker.getUserId().then((userId: string) => console.log('User ID:', userId));
    this.matomoTracker
      .getVisitorId()
      .then((visitorId: string) => console.log('Visitor ID:', visitorId));
    this.matomoTracker
      .getVisitorInfo()
      .then((visitorInfo: string[]) => console.log('Visitor Info:', visitorInfo));
    this.matomoTracker
      .hasCookies()
  }
}
