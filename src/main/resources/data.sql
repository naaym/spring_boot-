INSERT INTO products (id, name, description, price, original_price)
VALUES
    ('whey-isolate', 'Whey Isolate 1kg', 'Protéine premium pour la récupération.', 99.00, 129.00),
    ('creatine', 'Créatine Monohydrate 300g', 'Force et explosivité garanties.', 49.00, NULL),
    ('fitness-bundle', 'Pack Performance', 'Shaker + gants + barre protéinée.', 69.00, 85.00)
ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    description = VALUES(description),
    price = VALUES(price),
    original_price = VALUES(original_price);
