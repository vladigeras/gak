import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import { MenuComponent } from './component/menu/menu.component';
import { FooterComponent } from './component/footer/footer.component';
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";
import {HttpClientModule} from "@angular/common/http";
import {ToastModule, ToastOptions} from 'ng2-toastr/ng2-toastr';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {LoginModalComponent} from "./component/modal/login-modal/login-modal.component";
import {UserAddModalComponent} from "./component/modal/user-add-modal/user-add-modal.component";
import {UserService} from "./service/user.service";
import { AngularMultiSelectModule } from 'angular2-multiselect-dropdown/angular2-multiselect-dropdown';
import {FormsModule} from "@angular/forms";

export class CustomToastOptions extends ToastOptions {
  showCloseButton = true;
  enableHTML = true;
  maxShown = 20;
}

@NgModule({
  declarations: [
    AppComponent,
    MenuComponent,
    FooterComponent,
    LoginModalComponent,
    UserAddModalComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    FormsModule,
    NgbModule.forRoot(),
    ToastModule.forRoot(),
    AngularMultiSelectModule
  ],
  providers: [
    {provide: ToastOptions, useClass: CustomToastOptions},
  UserService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
