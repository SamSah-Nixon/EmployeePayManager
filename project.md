# Employee Pay Manager
> Our company currently maintains all employee records in physical form, including details such as names, employee ID numbers, date of birth, salaries, address, and work history. We are looking to digitize our records and transition to a computerized system that can store, manage, and update this information efficiently.
> Additionally, our employees track their working hours using punch cards, and we would like the new system to incorporate an automated timekeeping feature. This system should enable employees to clock in and out electronically, accurately recording their hours worked.
> We also require the system to generate pay reports on-demand for our payroll processes, considering that paydays vary.
> Furthermore, we occasionally shut down our system overnight or for extended periods, such as during long vacations. Upon rebooting, the system must automatically restore to its previous state without requiring manual intervention.

## important parts
* a computerized system that can store, manage, and update this information efficiently.
  * names, employee ID numbers, date of birth, salaries, address, and work history, BIRTHDAYS
* incorporate an automated timekeeping feature. 
  * clock in and out electronically, accurately recording their hours worked.
* generate pay reports on-demand for our payroll processes, considering that paydays vary.
* automatically restore to its previous state (upon restart) without requiring manual intervention


so:
* keep the employees as an array of json objects with the info (or a map of id->info?)
* Keep a separate json object with each employee's hours worked (have some sort of range system)
  * maybe keep them together? java file io is hilariously slow
* I don't really get what "paydays vary" means but that should just be simple math with calculating the hours worked times the salary
* restoring should be as simple as a shutdown hook

some salary employees, some hourly employees
salary: yearly but / by 365 and * by number of days in the pay period (variable number of days)
if salary employees don't work >=40 hrs/week then they lose a day's pay for every 8 hours they're under

clocking in:
- only by id (they are secret)

- temporary employees exist too (hourly)

company address: 3 five cedar rye land new york 11122-1111
colors: blue, gold, gray, black, white

id's can have numbers/alphanumeric letters, any length
admin id: 000100

sort employees by different things like first/last name, 

overtime: +40 hours in a week OR 9 days in a row = 1.5x pay
- ONLY for hourly employees
- this stacks (both = 2.25x)

search functionality to get employee information

everybody is a contracted employee (no taxes)
no benefits
delete and edit records in the CURRENT pay period only
