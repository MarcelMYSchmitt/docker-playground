import { Component } from '@angular/core';

@Component({
  selector: 'app-elk',
  templateUrl: './elk.component.html',
  styleUrls: ['./elk.component.css']
})

export class ElkComponent {

  errorText = "";

  whatHappensOnClickError() {
    let errorMessage ="This is a test error for APM service in Kibana."
    this.errorText = errorMessage;

    throw new Error();
  }
}