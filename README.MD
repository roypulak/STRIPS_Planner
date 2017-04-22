Intelligent strategies are compulsory for solving problems arising in our daily life. Knowledge based intelligent strategies are prerequisite to solve such problems efficiently and to
achieve the desired result. Planning refers to a sequence of actions that lead from the
initial state to the goal state.

Problem solving and planning are deeply related to Artificial Intelligence. For instance
in the fields of game playing, robots utilize problem solving and planning techniques to
accomplish their tasks.

The Standford Research Institute Problem Solver (STRIPS) is an automated planning
technique that works by executing a domain and problem to find a goal. A characteristic of this planning systems is their linear approach, i.e. only one aim is followed and
attempted to be solved { interleaving of goals is not allowed. Goals are maintained on
a stack and dealt with one by one. Additionally, only totally ordered action sequences
are allowed, i.e. the order of actions cannot be changed later on and must be strictly
determined once planning is complete. With STRIPS you provide objects, actions, preconditions, and effects. Once the domain is described, you then provide the initial state
and final state. Strips can then search all possible states, starting from the initial one,
executing various actions, until it reaches the goal.


In this exercise our problem description is like, there is a squared building composed
by 36 offices, which are located in a matrix of 6 rows and 6 columns. From each office it
is possible to move (horizontally or vertically) to the adjacent offices. The building has
some coffee machines in some offices that can make 1, 2 or 3 cups of coffee at one time.
The people working at the offices may ask for coffee and a robot called \Clooney" is in
charge of serving the coffees required. Each office may ask for 1, 2 or 3 coffees but not
more. The petitions of coffee are done all at early morning so that the robot can plan
the service procedure. Each petition has to be served in a single service. The goal is to
serve all the drinks to all the offices in an efficient way (minimizing the travel inside the
building, in order to not disturb the people working). The robot will start with a given
initial state. Initial state contains petitions of coffee, positions of the coffee machines and
the initial position of the robot. In goal state no more petitions are pending to be served.
Following figures depict our problem scenario more clearly.

---------------------------------------------------------------------------
Detail of the implementation is explained in Report Coffee Server - Monica and Pulak.pdf file.