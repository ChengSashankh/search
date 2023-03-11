import {AppConfiguration} from "./app-configuration";

export class Optional<T> {
  private initialized: boolean;
  private value: T | undefined;

  constructor() {
    this.initialized = false;
    this.value = undefined;
  }

  public isEmpty(): boolean {
    return this.initialized;
  }

  public get(): T {
    if (!this.initialized) {
      throw new Error("get() called on empty optional");
    }

    return this.value as T;
  }

  public getOrElse(fallBack: T): T {
    return this.initialized ? this.value as T : fallBack;
  }

  public set(newValue: T): void {
    this.initialized = true;
    this.value = newValue;
  }

  public unset(): void {
    this.initialized = false;
    this.value = undefined;
  }
}
