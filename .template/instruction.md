#### Consumer Driven Contract (CDC) testing is a method where the contract between microservices is defined by the consumer, and the provider adheres to this contract. This contract is then used to conduct tests.

#### It can be used for synchronous calls using HTTP REST API and asynchronous calls based on messaging.
#### Contract Testing offers the following benefits:

#### 1.Ensures Service Independence

#### It helps each service to be developed and deployed independently. By defining the contract by the consumer and verifying that the provider adheres to it, dependencies between services can be minimized.

#### 2.Reduces Communication Errors

#### It allows for the early detection and correction of errors that may occur in communication between services. This is possible because the contract between services is clearly defined and tested.

#### 3. Provides Fast Feedback Loop

#### CDC testing provides quick feedback when the contract between the consumer and provider changes. This allows developers to immediately see the impact of changes on other services.

#### 4.Documentation

#### The contract itself clearly documents the communication between services. This helps new team members or other teams understand the interactions between services.

#### 5.Enhances Reliability

#### By clearly defining and testing the contract between services, the overall system reliability is improved. This ensures that services will behave as expected.

#### 6. Change Management

#### CDC testing helps manage changes to the contract between services. When the contract changes, tests can be updated based on it, and the impact on other services can be assessed.

### How to Create Contract Test

#### 1. In the modeling phase, create a Policy sticker and attach it to the Aggregate sticker.

#### 2. To create examples using the Given-When-Then pattern, set up Relations as Event - Policy - Event or Event - Command - Event

#### 3. Click 'Examples' on the Policy sticker panel and create examples following the Given-When-Then pattern.
