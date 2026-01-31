import { Component, ChangeDetectionStrategy, input, output, signal, effect, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import {
  IonHeader,
  IonToolbar,
  IonTitle,
  IonButtons,
  IonButton,
  IonContent,
  IonItem,
  IonLabel,
  IonInput,
  IonToggle,
  IonText,
  ModalController
} from '@ionic/angular/standalone';
import { User, CreateUserRequest, UpdateUserRequest } from '../../models/user.model';

@Component({
  selector: 'app-user-form',
  imports: [
    ReactiveFormsModule,
    IonHeader,
    IonToolbar,
    IonTitle,
    IonButtons,
    IonButton,
    IonContent,
    IonItem,
    IonLabel,
    IonInput,
    IonToggle,
    IonText
  ],
  templateUrl: './user-form.component.html',
  styleUrl: './user-form.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class UserFormComponent {
  user = input<User | null>(null);

  formSubmit = output<CreateUserRequest | UpdateUserRequest>();
  formCancel = output<void>();

  userForm: FormGroup;
  isEditMode = signal<boolean>(false);
  private modalController = inject(ModalController);

  constructor(private fb: FormBuilder) {
    this.userForm = this.fb.group({
      email: ['', [Validators.required, Validators.email, Validators.maxLength(100)]],
      firstName: ['', [Validators.required, Validators.maxLength(100)]],
      lastName: ['', [Validators.required, Validators.maxLength(100)]],
      active: [true]
    });

    // Update form when user input changes
    effect(() => {
      const currentUser = this.user();
      if (currentUser) {
        this.isEditMode.set(true);
        this.userForm.patchValue({
          email: currentUser.email,
          firstName: currentUser.firstName,
          lastName: currentUser.lastName,
          active: currentUser.active
        });
      } else {
        this.isEditMode.set(false);
        this.userForm.reset({ active: true });
      }
    });
  }

  async onSubmit() {
    if (this.userForm.valid) {
      const formValue = this.userForm.value;

      if (this.isEditMode()) {
        // Edit mode: only send changed fields
        const updateRequest: UpdateUserRequest = {};
        const currentUser = this.user();

        if (currentUser && formValue.email !== currentUser.email) {
          updateRequest.email = formValue.email;
        }
        if (currentUser && formValue.firstName !== currentUser.firstName) {
          updateRequest.firstName = formValue.firstName;
        }
        if (currentUser && formValue.lastName !== currentUser.lastName) {
          updateRequest.lastName = formValue.lastName;
        }
        if (currentUser && formValue.active !== currentUser.active) {
          updateRequest.active = formValue.active;
        }

        await this.modalController.dismiss(updateRequest, 'submit');
      } else {
        // Create mode: send all required fields
        const createRequest: CreateUserRequest = {
          email: formValue.email,
          firstName: formValue.firstName,
          lastName: formValue.lastName
        };
        await this.modalController.dismiss(createRequest, 'submit');
      }
    }
  }

  async onCancel() {
    await this.modalController.dismiss(null, 'cancel');
  }

  getErrorMessage(fieldName: string): string {
    const field = this.userForm.get(fieldName);
    if (field?.hasError('required')) {
      return `${this.capitalize(fieldName)} is required`;
    }
    if (field?.hasError('email')) {
      return 'Please enter a valid email address';
    }
    if (field?.hasError('maxlength')) {
      return `${this.capitalize(fieldName)} must be less than 100 characters`;
    }
    return '';
  }

  private capitalize(str: string): string {
    return str.charAt(0).toUpperCase() + str.slice(1);
  }
}
