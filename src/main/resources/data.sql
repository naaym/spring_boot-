INSERT INTO categories (name, image_url)
VALUES
    ('Protéines', 'https://images.unsplash.com/photo-1605296867304-46d5465a13f1'),
    ('Créatine', 'https://images.unsplash.com/photo-1502741338009-cac2772e18bc'),
    ('Accessoires', 'https://images.unsplash.com/photo-1579758629938-03607ccdbaba')
ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    image_url = VALUES(image_url);

INSERT INTO products (id, name, description, price, original_price, category_id, image_url)
VALUES
    ('whey-isolate', 'Whey Isolate 1kg', 'Protéine premium pour la récupération.', 99.00, 129.00, (SELECT id FROM categories WHERE name = 'Protéines'), 'https://images.unsplash.com/photo-1586401100295-7a8096fd2315'),
    ('creatine', 'Créatine Monohydrate 300g', 'Force et explosivité garanties.', 49.00, NULL, (SELECT id FROM categories WHERE name = 'Créatine'), 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b'),
    ('fitness-bundle', 'Pack Performance', 'Shaker + gants + barre protéinée.', 69.00, 85.00, (SELECT id FROM categories WHERE name = 'Accessoires'), 'https://images.unsplash.com/photo-1508182311256-e3f9f1403980')
ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    description = VALUES(description),
    price = VALUES(price),
    original_price = VALUES(original_price),
    category_id = VALUES(category_id),
    image_url = VALUES(image_url);
