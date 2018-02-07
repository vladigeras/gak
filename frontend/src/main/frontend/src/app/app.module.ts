import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {MenuComponent} from './component/menu/menu.component';
import {FooterComponent} from './component/footer/footer.component';
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";
import {HttpClientModule} from "@angular/common/http";
import {ToastModule, ToastOptions} from 'ng2-toastr/ng2-toastr';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {LoginModalComponent} from "./component/modal/login-modal/login-modal.component";
import {UserAddModalComponent} from "./component/modal/user-add-modal/user-add-modal.component";
import {UserService} from "./service/user.service";
import {AngularMultiSelectModule} from 'angular2-multiselect-dropdown/angular2-multiselect-dropdown';
import {FormsModule} from "@angular/forms";
import {UsersTableComponent} from './component/users-table/users-table.component';
import {NgxDatatableModule} from '@swimlane/ngx-datatable';
import {AuthService} from "./security/auth.service";
import {CookieService} from 'ngx-cookie-service';
import {AdminPanelComponent} from './pages/admin-panel/admin-panel.component';
import {MainPageComponent} from './pages/main-page/main-page.component';
import {RouterModule, Routes} from "@angular/router";
import {NotFoundPageComponent} from './pages/not-found-page/not-found-page.component';
import {MemberPanelComponent} from './pages/member-panel/member-panel.component';
import {SecretaryPanelComponent} from './pages/secretary-panel/secretary-panel.component';
import {PresidentPanelComponent} from './pages/president-panel/president-panel.component';
import {DndModule} from 'ng2-dnd';
import {StudentSpeechByDateComponent} from './component/student-speech-by-date/student-speech-by-date.component';
import {StudentService} from "./service/student.service";
import {StudentAddModalComponent} from './component/modal/student-add-modal/student-add-modal.component';
import {StudentsTableComponent} from './component/students-table/students-table.component';
import {OWL_DATE_TIME_LOCALE, OwlDateTimeModule, OwlNativeDateTimeModule} from 'ng-pick-datetime';
import {SpeakerService} from "./service/speaker.service";
import {CommissionsTableComponent} from './component/commissions-table/commissions-table.component';
import {CommissionService} from "./service/commission.service";

export class CustomToastOptions extends ToastOptions {
  showCloseButton = true;
  enableHTML = true;
  maxShown = 20;
}

const routes: Routes = [
  {path: '', component: MainPageComponent},
  {path: 'president', component: PresidentPanelComponent},
  {path: 'member', component: MemberPanelComponent},
  {path: 'secretary', component: SecretaryPanelComponent},
  {path: 'admin', component: AdminPanelComponent},
  {path: '**', component: NotFoundPageComponent}
];

@NgModule({
  declarations: [
    AppComponent,
    MenuComponent,
    FooterComponent,
    LoginModalComponent,
    UserAddModalComponent,
    UsersTableComponent,
    AdminPanelComponent,
    MainPageComponent,
    NotFoundPageComponent,
    MemberPanelComponent,
    SecretaryPanelComponent,
    PresidentPanelComponent,
    StudentSpeechByDateComponent,
    StudentAddModalComponent,
    StudentsTableComponent,
    CommissionsTableComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    FormsModule,
    NgbModule.forRoot(),
    ToastModule.forRoot(),
    AngularMultiSelectModule,
    NgxDatatableModule,
    RouterModule.forRoot(routes),
    DndModule.forRoot(),
    OwlDateTimeModule,
    OwlNativeDateTimeModule
  ],
  providers: [
    {provide: ToastOptions, useClass: CustomToastOptions},
    UserService,
    AuthService,
    CookieService,
    StudentService,
    SpeakerService,
    CommissionService,
    {provide: OWL_DATE_TIME_LOCALE, useValue: 'ru'}
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
