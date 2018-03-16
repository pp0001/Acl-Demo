INSERT INTO Users (id, email, password, role_id) VALUES
(1, 'user1@mail.com', 'user1', 'procedure_owner'),
(2, 'user2@mail.com', 'user2', 'procedure_owner'),
(3, 'user3@mail.com', 'user3', 'admin'),
(4, 'user4@mail.com', 'user4', 'employee');

INSERT INTO Ropa (id, name) VALUES
(1, 'Ropa1'),
(2, 'Ropa2'),
(3, 'Ropa3');

INSERT INTO ropa_owner (id, owner_id, ropa_id) VALUES
(1, 1, 1),
(2, 2, 2),
(3, 3, 3);

INSERT INTO acl_sid (id, principal, sid) VALUES
(1, 0, 'ROLE_PO'), 		-- procedure_owner == ROLE_PO
(2, 0, 'ROLE_USER'),  	-- employee == ROLE_USER
(3, 0, 'ROLE_ADMIN'); 	-- admin == ROLE_ADMIN

INSERT INTO acl_class (id, class) VALUES 
(1, 'com.process.demo.model.Ropa');

INSERT INTO acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES
(1, 1, 1, NULL, 1, 1), -- Ropa1 object identity
(2, 1, 2, NULL, 1, 1), -- Ropa2 object identity
(3, 1, 3, NULL, 1, 1); -- Ropa3 object identity

INSERT INTO acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES
(1, 1, 0, 3, 16,  1, 0, 0),		-- admin has Admin permission for Ropa1
(2, 2, 0, 3, 16,  1, 0, 0),  	-- admin has Admin permission for Ropa2
(3, 3, 0, 3, 16, 1, 0, 0), 		-- admin has Admin permission for Ropa3
(4, 1, 1, 2, 1,  1, 0, 0), 		-- employee has Read permission for Ropa1
(5, 2, 1, 2, 1,  1, 0, 0), 		-- employee has Read permission for Ropa2
(6, 3, 1, 2, 1,  1, 0, 0), 		-- employee has Read permission for Ropa3
(7, 1, 2, 1, 32,  1, 0, 0),  	-- procedure_owner has OWNER_CRU permission for Ropa1
(8, 2, 3, 1, 32,  1, 0, 0),  	-- procedure_owner has OWNER_CRU permission for Ropa2
(9, 3, 4, 1, 32, 1, 0, 0); 		-- procedure_owner has OWNER_CRU permission for Ropa3



