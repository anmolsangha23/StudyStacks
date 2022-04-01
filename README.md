# StudyStacks

### Flashcard software to help you study more effectively.

StudyStacks is an application that serves to help students and learners create and manage flashcards they can use daily 
to reinforce their learning. Users can create ***stacks*** of their own cards with information on both sides, customized to 
their liking. They are able ***study*** their cards in sequential or random order. Being a university student myself,
I found that creating an application to help myself and other students learn more effectively would be interesting as I
am able to understand the problem domain particularly well. Students are always on the hunt for tools to help them study
more efficiently and StudyStacks aims to be just that.

StudyStacks may be helpful if you find yourself in any of these situations: 
- You are a university student studying for midterms or finals.
- You find yourself in a foreign country and need to learn the language.
- You are meeting your girlfriend's parents soon and need to memorize facts about her dad's favourite football team.


### User Stories

- As a user, I want to be able to add a new card to my stack of cards.
- As a user, I want to be able to flag a card I am having trouble with.
- As a user, I want to be able to delete any stacks I have no need for.
- As a user, I want to be able to view the cards in my stack in a random order.
- As a user, I want to be able to save my card stacks to a file.
- As a user, I want to be able to load my card stacks from a file previously saved.

### Phase 4: Task 2

#### Sample of EventLog occurrences

Fri Apr 01 09:34:18 PDT 2022
New card added to CPSC 210

Fri Apr 01 09:34:22 PDT 2022
Card flag status updated to flagged

Fri Apr 01 09:34:24 PDT 2022
Card flag status updated to unflagged

### Phase 4: Task 3

The design of this program is quite well done in my humble opinion. If I had to implement changes, they would be as follows:
- I would look into refactoring the implementation in StudyStacks such that the GUI can access the current stack by 
checking the list of all stacks rather than having a separate field for the current CardStack.
- I would also introduce hierarchies and refactor the GUI such that all the inner classes for visuals such as buttons and 
frames so that the code is easier to update if I want to make the same visual changes to all buttons at the same time.


