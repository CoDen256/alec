package coden.alec.core

import java.lang.RuntimeException

interface CreateFactorInteractor: Interactor
interface DeleteFactorInteractor: Interactor
interface PurgeFactorInteractor: Interactor
interface UpdateFactorInteractor: Interactor
interface ListFactorsInteractor: Interactor

interface CreateScaleInteractor: Interactor
interface DeleteScaleInteractor: Interactor
interface PurgeScaleInteractor: Interactor
interface UpdateScaleInteractor: Interactor
interface ListScalesInteractor: Interactor

class ScaleIsNotDeletedException(val scaleId: String): RuntimeException()