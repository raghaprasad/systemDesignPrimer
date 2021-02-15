class Visitor:
    def visit(self, item):
        pass

class Visitable:
    def __init__(self):
        pass

    def accept(self, v: Visitor):
        pass


'''
Sum of numbers (list of dict)
'''
class SumVisitor(Visitor):
    def __init__(self):
        self.sum = 0
    
    def visit(self, item):
        self.sum = self.sum + item

    def value(self):
        return self.sum


class VisitableList(Visitable):
    def __init__(self, list_to_visit):
        self.list_to_visit = list_to_visit

    def accept(self, v: Visitable):
        for i in self.list_to_visit:
            v.visit(i)

class VisitableMap(Visitable):
    def __init__(self, map_to_visit):
        self.map_to_visit = map_to_visit
    
    def accept(self, v: Visitable): 
        for k in self.map_to_visit.keys():
            v.visit(self.map_to_visit[k])

list_of_nums = [1,2,3,4]
dict_of_nums = { "1": 1, "2": 2, "3": 3, "4": 5 }

s = SumVisitor()
v1 = VisitableList(list_of_nums)
v1.accept(s)
print(s.value())

s = SumVisitor()
v2 = VisitableMap(dict_of_nums)
v2.accept(s)
print(s.value())