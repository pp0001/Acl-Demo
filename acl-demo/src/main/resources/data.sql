INSERT INTO User (id, email, password, role_id) VALUES
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
(1, 1, 'user1@mail.com'), 	
(2, 1, 'user2@mail.com'),  	
(3, 1, 'user3@mail.com'),  	
(4, 1, 'user4@mail.com'); 

INSERT INTO acl_class (id, class) VALUES 
(1, 'com.process.demo.model.Ropa');

INSERT INTO acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES
(1, 1, 1, NULL, 1, 1), -- Ropa1 object identity
(2, 1, 2, NULL, 2, 1), -- Ropa2 object identity
(3, 1, 3, NULL, 3, 1); -- Ropa3 object identity

INSERT INTO acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES
(1, 1, 0, 3, 16,  1, 0, 0),		-- user3@mail.com has Admin permission for Ropa1
(2, 2, 0, 3, 16,  1, 0, 0),  	-- user3@mail.com has Admin permission for Ropa2
(3, 3, 0, 3, 16, 1, 0, 0), 		-- user3@mail.com has Admin permission for Ropa3
(4, 1, 1, 4, 1,  1, 0, 0), 		-- user4@mail.com has Read permission for Ropa1
(5, 2, 1, 4, 1,  1, 0, 0), 		-- user4@mail.com has Read permission for Ropa2
(6, 3, 1, 4, 1,  1, 0, 0), 		-- user4@mail.com has Read permission for Ropa3
(7, 1, 2, 1, 32,  1, 0, 0),  	-- user1@mail.com has OWNER_CRU permission for Ropa1
(8, 2, 2, 1, 1,  1, 0, 0),  	-- user1@mail.com has Read permission for Ropa2
(9, 3, 2, 1, 1, 1, 0, 0), 		-- user1@mail.com has Read permission for Ropa3
(10, 1, 3, 2, 1,  1, 0, 0),  	-- user2@mail.com has Read permission for Ropa2
(11, 2, 3, 2, 32,  1, 0, 0),  	-- user2@mail.com has OWNER_CRU permission for Ropa2
(12, 3, 3, 2, 1,  1, 0, 0);  	-- user2@mail.com has Read permission for Ropa3



