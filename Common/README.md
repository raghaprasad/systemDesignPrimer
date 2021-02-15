## How to think about system design

1. Establish a set of crisp I can statements - break down the scope, pick and choose one to solve for
   1. Always call out customer data as RED (highly confidential)
   2. Call out security considerations / but do not venture deep (not an area of expertise)
2. List out the main entities, keep then separate as much as possible 
   1. Even better is to list out interfaces (rather than objects)
3. Usage characteristics 
   1. Read heavy compared to write 
   2. Latency requirements 
   3. Transactional nature / Storage & access requirements 
4. Other things to keep in mind
   1. Reuse as much as possible 
   2. Interface everything 
