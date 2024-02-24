INSERT INTO container (container_name) 
VALUES
('MainPantry'),
('Cereal Pantry'),
('MiniFridge');

INSERT INTO item (name, container, quantity, expiry, fg, fresh)
VALUES
('GoudaCheese','MainPantry',12, '2024-05-12', 'Dairy', 'Fresh'),
('FruitLoops', 'MainPantry', 1, '2024-08-1', 'Grain', 'Fresh'),
('JollyRanchersBox', 'MainPantry', 2, '2024-4-14', 'Fruit', 'Fresh');