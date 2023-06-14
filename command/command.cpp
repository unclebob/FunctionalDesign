class Room {};

class Command {
  public:
    virtual void execute() = 0;
};

class UndoableCommand : public Command {
  public:
    virtual void undo() = 0;
};

class CommandWithArgument : public Command {
  public:
    CommandWithArgument(int argument)
		:argument(argument)
		{}

    virtual void execute()
    {theFunctionToExecute(argument);}

  private:
    int argument;

    void theFunctionToExecute(int argument)
    {
      //do something with that argument!
    }
};

class AddRoomCommand : public UndoableCommand {
  public:
    virtual void execute() {
      // manage canvas events to add room.
      // record what was done in theAddedRoom.
    }

    virtual void undo() {
      // remove theAddedRoom from the canvas.
    }

  private:
    Room* theAddedRoom;
};
