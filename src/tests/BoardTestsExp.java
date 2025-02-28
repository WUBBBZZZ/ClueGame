package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import experiment.TestBoard;

class BoardTestsExp {
	TestBoard board;
	
	@BeforeEach
	public void init() {
		board = new TestBoard();
	}
}
